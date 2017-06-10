import Model.FieldState;
import Model.Player;
import Protocol.Msg;
import Protocol.MsgType;
import Protocol.PlayerState;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ClientController {
    private static final Logger logger = LogManager.getLogger("Client");
    private Player player;
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private String address;
    private int port, numOfFieldsToTake, currentNumOfFieldsTaken, numOfShipsPlaced;
    private Node beginNode, endNode;

    @FXML
    private GridPane yourGrid;

    @FXML
    private GridPane enemyGrid;

    @FXML
    private Label status;

    @FXML
    private TextField serverAddress;

    @FXML
    private TextField serverPort;

    @FXML
    private Button connectButton;

    @FXML
    private Button finishedButton;

    @FXML
    private MenuButton shipsMenuBar;

    Task<Void> connectionHandler = new Task<Void>() {
        @Override protected Void call() throws Exception {
            logger.info("Thread is up");

            try {
                socket = new Socket(address, port);
                toServer = new ObjectOutputStream(socket.getOutputStream());
                fromServer = new ObjectInputStream(socket.getInputStream());

                Platform.runLater(() -> connectButton.setDisable(true));

                Msg msgFromServer;
                while((msgFromServer = (Msg) fromServer.readObject()) != null ) {
                    logger.info("Received message: " + msgFromServer.getMsgType());

                    switch (msgFromServer.getMsgType()) {
                        case SET_ID:
                            handleSetId(msgFromServer);
                            break;
                        case PLACE_SHIPS:
                            handlePlaceShips();
                            break;
                        case MAKE_MOVE:
                            handleMakeMove();
                            break;
                        case WAIT_FOR_MOVE:
                            handleWaitForMove();
                            break;
                        case SHOT_HIT:
                            handleShotHit(msgFromServer);
                            break;
                        case SHOT_MISS:
                            handleShotMiss(msgFromServer);
                            break;
                        case LOSE:
                            handleLose(msgFromServer);
                            break;
                        case WIN:
                            handleWin(msgFromServer);
                            break;
                    }
                }
            }
            catch (IOException e) {
                logger.info("Error while creating socket");
            }

            return null;
        }
    };

    private void send(Msg msgToServer) {
        try {
            toServer.writeObject(msgToServer);
        } catch (IOException e) {
            logger.info("Exception occurred while writing message to server");
        }
    }

    private void handleSetId(Msg msgFromServer) {
        Msg answer = new Msg();
        int playerId = msgFromServer.getPlayerID();

        player = new Player(playerId);
        player.setPlayerState(PlayerState.WAITING_FOR_GAME_BEGIN);
        answer.setMsgType(MsgType.ID_IS_SET);
        answer.setPlayerID(player.getPlayerId());
        send(answer);

        Platform.runLater(() -> {
            status.setText("Waiting for game to beginNode");
        });
    }

    private void handlePlaceShips() {
        player.setPlayerState(PlayerState.PLACING_SHIPS);

        Platform.runLater(() -> {
            shipsMenuBar.setDisable(false);
            status.setText("Choose ship from menu bar");
        });
    }

    private void handleMakeMove() {
        player.setPlayerState(PlayerState.MAKING_MOVE);

        Platform.runLater(() -> {
            status.setText("Make move");
            setGridIsDisable(enemyGrid, false);
        });
    }

    private void handleWaitForMove() {
        player.setPlayerState(PlayerState.WAITING_FOR_MOVE);

        Platform.runLater(() -> {
            status.setText("Wait for enemy's move");
        });
    }

    @FXML
    private void handleFinishedButtonFired() {
        new Thread(() -> {
            Msg answer = new Msg(MsgType.SHIPS_PLACED, player.getPlayerId(), player.getPlayerMap());
            send(answer);
            logger.info("Send message of type: " + MsgType.SHIPS_PLACED + " to the server");
        }).start();

        status.setText("Wait for server response");
        finishedButton.setDisable(true);
        setGridIsDisable(yourGrid, true);
    }

    @FXML
    private void handleConnectButtonFired() {
        address = serverAddress.getText();
        port = Integer.parseInt(serverPort.getText());

        Thread handlerThread = new Thread(connectionHandler);
        handlerThread.setName("Handler Thread");
        handlerThread.start();
    };

    @FXML
    private void handleMenuItemSelected(ActionEvent event) {
        status.setText("Place ship");
        shipsMenuBar.setDisable(true);

        setGridIsDisable(yourGrid, false);

        MenuItem menuItem = (MenuItem) event.getSource();
        menuItem.setDisable(true);

        PlayerState newPlayerState = null;
        switch (menuItem.getText()) {
            case "Carrier[Size 5]":
                newPlayerState = PlayerState.PLACING_CARRIER;
                numOfFieldsToTake = 5;
                break;
            case "Battleship[Size 4]":
                newPlayerState = PlayerState.PLACING_BATTLESHIP;
                numOfFieldsToTake = 4;
                break;
            case "Cruiser[Size 3]":
                newPlayerState = PlayerState.PLACING_CRUISER;
                numOfFieldsToTake = 3;
                break;
            case "Submarine[Size 3]":
                newPlayerState = PlayerState.PLACING_SUBMARINE;
                numOfFieldsToTake = 3;
                break;
            case "Destroyer[Size 2]":
                newPlayerState = PlayerState.PLACING_DESTROYER;
                numOfFieldsToTake = 2;
                break;
            default:
                logger.info("Problem while matching ship to new player state");
        }
        player.setPlayerState(newPlayerState);
    }

    private void handleYourGridCellButtonFired(MouseEvent event, Node node) {
        node.setDisable(true);
        node.setStyle("-fx-background-color: deepskyblue");
        ++currentNumOfFieldsTaken;

        player.getPlayerMap().setFieldState(GridPane.getRowIndex(node), GridPane.getColumnIndex(node), FieldState.SHIP);

        if ( currentNumOfFieldsTaken == numOfFieldsToTake ) {
            currentNumOfFieldsTaken = 0;
            ++numOfShipsPlaced;

            shipsMenuBar.setDisable(false);
            setGridIsDisable(yourGrid, true);

            if( numOfShipsPlaced == 5 ) {
                shipsMenuBar.setDisable(true);
                finishedButton.setDisable(false);
            }
        }
    }

    private void handleEnemyGridCellButtonFired(MouseEvent event, Node node) {
        node.setStyle("-fx-background-color: dimgrey");

        int[] shotCoordinates = { GridPane.getRowIndex(node), GridPane.getColumnIndex(node) };
        Msg shotMsg = new Msg(MsgType.SHOT_PERFORMED, player.getPlayerId(), shotCoordinates);

        new Thread(() -> {
            send(shotMsg);
            logger.info("Send message of type: " + MsgType.SHOT_PERFORMED + " to the server");
            logger.info("Coords of cell - ROW: " + GridPane.getRowIndex(node) + " COL: " + GridPane.getColumnIndex(node));
        }).start();

        status.setText("Wait for server response");
        setGridIsDisable(enemyGrid, true);
    }

    private void handleShotHit(Msg msgFromServer) {
        int row = ((int[])msgFromServer.getDataObj())[0];
        int col = ((int[])msgFromServer.getDataObj())[1];

        logger.info("ROW: " + row + ", COL: " + col);

        // This means updating player who just shot
        if( player.getPlayerId().equals(msgFromServer.getPlayerID()) ) {
            player.getEnemyMap().setFieldState(row, col, FieldState.HIT);
            Platform.runLater(() -> {
                enemyGrid.getChildren().get(row*10+col+1).setStyle("-fx-background-color: red");
            });
        }
        else { // This means updating player who was waiting
            player.getPlayerMap().setFieldState(row, col, FieldState.HIT);
            Platform.runLater(() -> {
                yourGrid.getChildren().get(row*10+col+1).setStyle("-fx-background-color: red");
            });
        }
    }

    private void handleShotMiss(Msg msgFromServer) {

        int row = ((int[])msgFromServer.getDataObj())[0];
        int col = ((int[])msgFromServer.getDataObj())[1];

        logger.info("ROW: " + row + ", COL: " + col);

        // This means updating player who just shot
        if( player.getPlayerId().equals(msgFromServer.getPlayerID()) ) {
            player.getEnemyMap().setFieldState(row, col, FieldState.HIT);
            Platform.runLater(() -> {enemyGrid.getChildren().get(row*10+col+1).setStyle("-fx-background-color: black");});
        }
        else { // This means updating player who was waiting
            player.getPlayerMap().setFieldState(row, col, FieldState.HIT);
            Platform.runLater(() -> {yourGrid.getChildren().get(row*10+col+1).setStyle("-fx-background-color: black");});
        }
    }

    private void handleLose(Msg msgFromServer) {
        int row = ((int[])msgFromServer.getDataObj())[0];
        int col = ((int[])msgFromServer.getDataObj())[1];

        player.getPlayerMap().setFieldState(row, col, FieldState.HIT);
        Platform.runLater(() -> {yourGrid.getChildren().get(row*10+col+1).setStyle("-fx-background-color: red");});
        Platform.runLater(() -> {
            status.setText("You have lost");
        });
    }

    private void handleWin(Msg msgFromServer) {
        int row = ((int[])msgFromServer.getDataObj())[0];
        int col = ((int[])msgFromServer.getDataObj())[1];

        player.getEnemyMap().setFieldState(row, col, FieldState.HIT);
        Platform.runLater(() -> {enemyGrid.getChildren().get(row*10+col+1).setStyle("-fx-background-color: red");});

        Platform.runLater(() -> {
            status.setText("You have won");
        });
    }

    @FXML
    private void initialize() {
        // TODO - this need to be set to true
        finishedButton.setDisable(false);
        shipsMenuBar.setDisable(true);
        status.setText("Not connected");

        currentNumOfFieldsTaken = 0;
        numOfShipsPlaced = 0;

        initGrids();
    }

    private void setGridIsDisable(GridPane gridPane, boolean isGridDisable ) {
        for(Node node: gridPane.getChildren()) {
            node.setDisable(isGridDisable);
        };
    }

    /**
     * Add buttons to player's and enemy's grid
     */
    private void initGrids() {
        GridPane[] grids = {yourGrid, enemyGrid};
        for(GridPane gridPane: grids) {

            for (int i = 0; i < 10; ++i) {
                for(int j = 0; j < 10; ++j) {
                    Button btn = new Button();
                    btn.setPrefWidth(28);
                    btn.setPrefHeight(30);
                    btn.setOpacity(0.5);
                    btn.setDisable(true);
                    GridPane.setColumnIndex(btn, j);
                    GridPane.setRowIndex(btn, i);
                    gridPane.getChildren().add(btn);
                    //gridPane.add(btn, j, i);
                }
            }
        }

        for (Node node: yourGrid.getChildren()) {
            node.setOnMouseClicked((event -> handleYourGridCellButtonFired(event, node)));
        }

        for (Node node: enemyGrid.getChildren()) {
            node.setOnMouseClicked((event -> handleEnemyGridCellButtonFired(event, node)));
        }
    }
}

import model.FieldState;
import model.Player;
import model.Coordinates;
import network.ConnectionHandler;
import protocol.Msg;
import protocol.MsgType;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller {
    private static final Logger logger = LogManager.getLogger("Client");
    private Player player;
    private int shipLength, currentNumOfFieldsTaken, numOfShipsPlaced;
    private ConnectionHandler connectionHandler;
    private Thread msgHandlerThread;
    private boolean isConnectionHandlerThreadUp, isMsgHandlerThreadUp;

    private Task msgHandler = new Task<Void>() {
        @Override public Void call() throws Exception {
            logger.info("Task is up");
            Msg msg;
            while( (msg = connectionHandler.getMessagesReceived().take()) != null)
            {
                Coordinates coordinates = (Coordinates)msg.getDataObj();
                Integer row = null;
                Integer col = null;
                if (coordinates != null ) {
                    row = coordinates.getRow();
                    col = coordinates.getCol();
                }

                switch(msg.getMsgType()) {
                    case SET_ID:
                        handleSetId(msg);
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
                    case HIT_MAKE_MOVE:
                        handleHitMakeMove(row, col);
                        break;
                    case HIT_WAIT_FOR_MOVE:
                        handleHitWaitForMove(row, col);
                        break;
                    case MISS_MAKE_MOVE:
                        handleMissMakeMove(row, col);
                        break;
                    case MISS_WAIT_FOR_MOVE:
                        handleMissWaitForMove(row, col);
                        break;
                    case LOSE:
                        handleLose(row, col);
                        break;
                    case WIN:
                        handleWin(row, col);
                        break;
                }

            }
            return null;
        }
    };

    public Controller() {
        player = new Player(null);
        isConnectionHandlerThreadUp = false;
        isMsgHandlerThreadUp = false;

    }

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

    @FXML
    private void initialize() {
        // TODO - SET IT TO FALSE WHEN PUT ON GITHUB
        finishedButton.setDisable(false);
        shipsMenuBar.setDisable(true);
        status.setText("Not connected");

        currentNumOfFieldsTaken = 0;
        numOfShipsPlaced = 0;
    }

    @FXML
    private void handleFinishedButtonFired() {
        Msg answer = new Msg(MsgType.SHIPS_PLACED, player.getPlayerId(), player.getPlayerMap());
        connectionHandler.addMessageToSend(answer);

        status.setText("Wait for server response");
        finishedButton.setDisable(true);
        setGridIsDisable(yourGrid, true);
    }

    @FXML
    private void handleConnectButtonFired() {
        serverAddress.setDisable(true);
        serverPort.setDisable(true);

        String address = serverAddress.getText();
        int port = Integer.parseInt(serverPort.getText());

        connectionHandler = new ConnectionHandler(address, port);
        connectionHandler.start();

        msgHandlerThread = new Thread(msgHandler);
        msgHandlerThread.setName("msgHandlerThread");
        msgHandlerThread.start();

        isConnectionHandlerThreadUp = true;
        isMsgHandlerThreadUp = true;
    };

    @FXML
    private void handleMenuItemSelected(ActionEvent event) {
        status.setText("Place ship");
        shipsMenuBar.setDisable(true);

        setGridIsDisable(yourGrid, false);

        MenuItem menuItem = (MenuItem) event.getSource();
        menuItem.setDisable(true);

        shipLength = switchMenuItem(menuItem);
    }

    @ FXML
    private void handleEnemyGridCellButtonFired(ActionEvent event) {
        setGridIsDisable(enemyGrid, true);

        Node node = (Node) event.getSource();

        Coordinates shotCoordinates = new Coordinates(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
        Msg shotMsg = new Msg(MsgType.SHOT_PERFORMED, player.getPlayerId(), shotCoordinates);

        connectionHandler.addMessageToSend(shotMsg);
    }

    @FXML
    private void handleYourGridCellButtonFired(ActionEvent event) {
        Node node = (Node) event.getSource();
        node.setDisable(true);
        node.setStyle("-fx-background-color: deepskyblue");
        ++currentNumOfFieldsTaken;

        logger.info("Curr: " + currentNumOfFieldsTaken + ", numOfFields: " + shipLength);

        player.getPlayerMap().setFieldState(GridPane.getRowIndex(node), GridPane.getColumnIndex(node), FieldState.SHIP);

        if ( currentNumOfFieldsTaken == shipLength) {
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

    private void handleSetId(Msg msg) {
        player.setPlayerId(msg.getPlayerID());
        connectionHandler.addMessageToSend(new Msg(MsgType.ID_IS_SET, msg.getPlayerID()));

        Platform.runLater(() -> {
            status.setText("Waiting for game to begin");
            connectButton.setDisable(true);
        });
    }

    private void handlePlaceShips() {
        Platform.runLater(() -> {
            shipsMenuBar.setDisable(false);
            status.setText("Choose ship from menu bar");
        });
    }

    private void handleMakeMove() {
        Platform.runLater(() -> {
            status.setText("Make move");
            setGridIsDisable(enemyGrid, false);
        });
    }

    private void handleWaitForMove() {
        connectionHandler.addMessageToSend(new Msg(MsgType.WAITING, player.getPlayerId()));

        Platform.runLater(() -> {
            status.setText("Wait for move");
        });
    }

    private void handleHitMakeMove(Integer row, Integer col) {
        Platform.runLater(() -> {
            status.setText("You have been shot! Your turn");
            yourGrid.getChildren().get(row*10+col).setStyle("-fx-background-color: red");
            setGridIsDisable(enemyGrid, false);
        });
    }

    private void handleHitWaitForMove(Integer row, Integer col) {
        connectionHandler.addMessageToSend(new Msg(MsgType.WAITING, player.getPlayerId()));

        Platform.runLater(() -> {
            status.setText("You have hit the enemy! Good job");
            enemyGrid.getChildren().get(row*10+col).setStyle("-fx-background-color: red");
            setGridIsDisable(enemyGrid, true);
        });
    }

    private void handleMissMakeMove(Integer row, Integer col) {
        Platform.runLater(() -> {
            status.setText("Enemy didn't hit you. Your move");
            yourGrid.getChildren().get(row*10+col).setStyle("-fx-background-color: black");
            setGridIsDisable(enemyGrid, false);
        });
    }

    private void handleMissWaitForMove(Integer row, Integer col) {
        connectionHandler.addMessageToSend(new Msg(MsgType.WAITING, player.getPlayerId()));

        Platform.runLater(() -> {
            status.setText("You didn't hit. Wait for move");
            enemyGrid.getChildren().get(row*10+col).setStyle("-fx-background-color: black");
            setGridIsDisable(enemyGrid, true);
        });
    }

    private void handleLose(Integer row, Integer col) {
        Platform.runLater(() -> {
            status.setText("You have lost");

            yourGrid.getChildren().get(row*10+col).setStyle("-fx-background-color: red");
            setGridIsDisable(enemyGrid, true);
        });
    }

    private void handleWin(Integer row, Integer col) {
        Platform.runLater(() -> {
            status.setText("You have won");

            enemyGrid.getChildren().get(row*10+col).setStyle("-fx-background-color: red");
            setGridIsDisable(enemyGrid, true);
        });
    }

    private int switchMenuItem(MenuItem menuItem) {
        int numOfFieldsForShip = 0;

        switch (menuItem.getText()) {
            case "Carrier[Size 5]":
                numOfFieldsForShip = 5;
                break;
            case "Battleship[Size 4]":
                numOfFieldsForShip = 4;
                break;
            case "Cruiser[Size 3]":
                numOfFieldsForShip = 3;
                break;
            case "Submarine[Size 3]":
                numOfFieldsForShip = 3;
                break;
            case "Destroyer[Size 2]":
                numOfFieldsForShip = 2;
                break;
            default:
                logger.info("Problem while matching ship to new player state");
                break;
        }

        return numOfFieldsForShip;
    }

    private void setGridIsDisable(GridPane gridPane, boolean isGridDisable ) {
        for(Node node: gridPane.getChildren()) {
            node.setDisable(isGridDisable);
        };
    }

    public void close() {
        if( isConnectionHandlerThreadUp && isMsgHandlerThreadUp ) {
            connectionHandler.closeSocket();
            msgHandlerThread.interrupt();
            connectionHandler.interrupt();
            logger.info("connectionHandler interrupted");
        }
    }
}

package controller;

import model.FieldState;
import model.Player;
import model.Coordinates;
import network.ConnectionHandler;
import protocol.Msg;
import protocol.MsgType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Controller {
    private static final Logger logger = LogManager.getLogger("Client");
    private Player player;
    private int shipLength;
    private int currentNumOfFieldsTaken;
    private int numOfShipsPlaced;
    private ConnectionHandler connectionHandler;
    private MsgHandler msgHandler;

    public Controller() {
        player = new Player(null);
    }

    ConnectionHandler getConnectionHandler() {
        return connectionHandler;
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
        finishedButton.setDisable(true);
        shipsMenuBar.setDisable(true);
        status.setText("Not connected");

        currentNumOfFieldsTaken = 0;
        numOfShipsPlaced = 0;
    }

    @FXML
    private void handleFinishedButtonFired() {
        Msg answer = new Msg(MsgType.SHIPS_PLACED, player.getPlayerId(), player.getPlayerMap());
        //connectionHandler.getMessagesToSend().add()(answer);
        connectionHandler.getMessagesToSend().add(answer);

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

        msgHandler = new MsgHandler(this);
        msgHandler.start();
    };

    @FXML
    private void handleMenuItemSelected(ActionEvent event) {
        status.setText("Place ship");
        shipsMenuBar.setDisable(true);

        setGridIsDisable(yourGrid, false);

        MenuItem menuItem = (MenuItem) event.getSource();
        menuItem.setDisable(true);

        shipLength = getShipLength(menuItem);
    }

    @ FXML
    private void handleEnemyGridCellButtonFired(ActionEvent event) {
        setGridIsDisable(enemyGrid, true);

        Node node = (Node) event.getSource();

        Coordinates shotCoordinates = new Coordinates(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
        Msg shotMsg = new Msg(MsgType.SHOT_PERFORMED, player.getPlayerId(), shotCoordinates);

        connectionHandler.getMessagesToSend().add(shotMsg);
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

    void handleSetId(Msg msg) {
        player.setPlayerId(msg.getPlayerID());
        connectionHandler.getMessagesToSend().add(new Msg(MsgType.ID_IS_SET, msg.getPlayerID()));

        Platform.runLater(() -> {
            status.setText("Waiting for game to begin");
            connectButton.setDisable(true);
        });
    }

    void handlePlaceShips() {
        Platform.runLater(() -> {
            shipsMenuBar.setDisable(false);
            status.setText("Choose ship from menu bar");
        });
    }

    void handleMakeMove() {
        Platform.runLater(() -> {
            status.setText("Make move");
            setGridIsDisable(enemyGrid, false);
        });
    }

    void handleWaitForMove() {
        connectionHandler.getMessagesToSend().add(new Msg(MsgType.WAITING, player.getPlayerId()));

        Platform.runLater(() -> {
            status.setText("Wait for move");
        });
    }

    void handleHitMakeMove(Integer row, Integer col) {
        updateGUI("You have been shot! Your turn", yourGrid, enemyGrid, false, "red",
                    row, col);
    }

    void handleHitWaitForMove(Integer row, Integer col) {
        connectionHandler.getMessagesToSend().add(new Msg(MsgType.WAITING, player.getPlayerId()));
        updateGUI("You have hit the enemy! Good job", enemyGrid, enemyGrid, true, "red",
                    row, col);
    }

    void handleMissMakeMove(Integer row, Integer col) {
        updateGUI("Enemy didn't hit you. Your move", yourGrid, enemyGrid, false, "black",
                    row, col);
    }

    void handleMissWaitForMove(Integer row, Integer col) {
        connectionHandler.getMessagesToSend().add(new Msg(MsgType.WAITING, player.getPlayerId()));
        updateGUI("You didn't hit. Wait for move", enemyGrid, enemyGrid, true, "black",
                    row, col);
    }

    void handleLose(Integer row, Integer col) {
        updateGUI("You have lost", yourGrid, enemyGrid, true, "red", row, col);
    }

    void handleWin(Integer row, Integer col) {
        updateGUI("You have won", enemyGrid, enemyGrid, true, "red", row, col);
    }

    /**
     *
     * @param statusVal Text which will appear in status TextField
     * @param gridToUpdate Grid which will be updated with a shot result
     * @param gridToChangeIsDisable Grid which status will be changed to enable or prevent shooting
     * @param newGridState A value to which gridToChangeIsDisable will be changed
     * @param color Color which will appear in cell of updated grid
     * @param row Row of cell to update
     * @param col Column of cell to update
     */
    void updateGUI(String statusVal, GridPane gridToUpdate, GridPane gridToChangeIsDisable, boolean newGridState,
                     String color, Integer row, Integer col) {
        Platform.runLater(() -> {
            status.setText(statusVal);

            gridToUpdate.getChildren().get(row * 10 + col).setStyle("-fx-background-color: " + color);
            setGridIsDisable(gridToChangeIsDisable, newGridState);
        });
    }

    private Integer getShipLength(MenuItem menuItem) {
        HashMap<String, Integer> tmp = new HashMap<>();
        tmp.put("Carrier[Size 5]", 5);
        tmp.put("Battleship[Size 4]", 4);
        tmp.put("Cruiser[Size 3]", 3);
        tmp.put("Submarine[Size 3]", 3);
        tmp.put("Destroyer[Size 2]", 2);

        return tmp.get(menuItem.getText());
    }

    private void setGridIsDisable(GridPane gridPane, boolean isGridDisable ) {
        for(Node node: gridPane.getChildren()) {
            node.setDisable(isGridDisable);
        };
    }

    public void close() {
        if( connectionHandler != null ) {
            connectionHandler.closeConnection();
        }

        if( msgHandler != null && msgHandler.isAlive() ) {
           msgHandler.interrupt();
        }
        logger.info("ConnectionHandler interrupted");
    }
}

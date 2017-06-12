import network.GameServer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.paint.Paint;
import javafx.event.ActionEvent;

public class ServerController {
    private static final Logger logger = LogManager.getLogger("Server");
    private  GameServer gs;

    public ServerController() {
    }

    @FXML
    private Text serverStatus;

    @FXML
    private TextField portNumber;

    @FXML
    private Button startServerButton;

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleStartServerButtonClicked(ActionEvent event) {
        startServerButton.setDisable(true);

        String portNumberString = portNumber.getText();
        int port;

        try {
            port = Integer.parseInt(portNumberString);
        }
        catch(NumberFormatException e) {
            logger.error("Wrong port number. Using default port number: 4444");
            port = 4444;
        }
        gs = new GameServer(port);
        gs.start();

        serverStatus.setText("Server is running");
        serverStatus.setFill(Paint.valueOf("Green"));
    }

    void closeGameServer() {
        try {
            gs.closeServer();
            gs.interrupt();
        }
        catch (Exception e) {
            logger.warn("Exception occurred during server close");
        }
    }
}

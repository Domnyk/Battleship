import java.io.*;
import java.net.*;
import Protocol.Msg;
import Protocol.MsgType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane layout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Client");

        initLayout();
    }

    private void initLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientController.class.getResource("Client.fxml"));
            layout = loader.load();

            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.show();

            //ClientController controller = loader.getController();

            //scene.getWindow().setOnCloseRequest((WindowEvent event) -> {
             //   controller.closeGameServer();
               // Platform.exit();
            //});
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


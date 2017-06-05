import java.io.*;
import java.net.*;
import Protocol.Msg;
import Protocol.MsgType;

public class Main {
    public static void main(String[] args) {
        int id = -1;

        try (Socket socket = new Socket("localhost", 4444);
             ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
        ) {
           Msg msgFromServer;
           boolean isFirst = true;

           System.out.println("Test");

           while((msgFromServer = (Msg) fromServer.readObject()) != null ) {
               System.out.println("Received message of type " + msgFromServer.getMsgType());

               switch (msgFromServer.getMsgType()) {
                   case SET_ID:
                       id = msgFromServer.getPlayerID();
                       System.out.println("My ID is: " + id);
                       toServer.writeObject(new Msg(MsgType.ESTABLISH_CONNECTION, id));
                       break;
               }
           }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
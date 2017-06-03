package Network;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Connection extends Thread {
    private int connectionId;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public LinkedList<String> messagesQueue;

    private static final Logger logger = LogManager.getLogger("Server");

    public Connection(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        messagesQueue = new LinkedList<String>();
        this.socket = socket;
    }

    public void close() {
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendData(String data) {
        out.println(data);
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public void run() {
        this.setName("Connection with thread id " + String.valueOf(this.getId()));

        String s;
        try {
            while((s = in.readLine()) != null ) {
                messagesQueue.add(s);
            }
            out.close();
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package Network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Connection extends Thread {
    private String id;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public LinkedList<String> messagesQueue;

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

    @Override
    public void run() {
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

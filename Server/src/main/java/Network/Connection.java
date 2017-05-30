package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Connection extends Thread {
    private Socket client = null;
    private PrintWriter out;
    private BufferedReader in;

    public Connection(Socket client) {
        this.client = client;

        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        out.println("HELLO FROM THE SERVER YOU MOTHERFUCKER");
    }

    @Override
    public void run() {

    }
}

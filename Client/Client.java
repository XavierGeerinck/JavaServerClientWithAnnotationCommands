package Client;

import Client.commands.Replayer;
import Command.CommandMgr;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.SenderUtil;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private CommandMgr commandMgr;
    
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.socket = null;
        this.commandMgr = new CommandMgr();
        this.commandMgr.loadCommands(Replayer.class);
        
        this.connect();
        SenderUtil.sendMessage(this.socket, "Socket " + this.socket.getLocalSocketAddress() + " says hello");
        this.waitForMessages();
    }
    
    private void connect() {
        // Open the socket connection
        try {
            this.socket = new Socket(host, port);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(Client.class.getName()).log(Level.INFO, null, "Could not open connection to the server");
            throw new RuntimeException("Could not open connection to the server");
        }
    }
    
    /**
     * Wait for messages from the server (on a new thread! seeing that we want our main thread accessible)
     */
    private void waitForMessages() {
        new Thread() {
            @Override
            public void run() {
                System.out.println("[Client] Waiting for messages");
                try {
                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    while (socket.isConnected()) {
                        String message = in.readUTF();
                        
                        System.out.println("[Client] Received message from server: " + message);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }

    public Socket getSocket() {
        return socket;
    }
}

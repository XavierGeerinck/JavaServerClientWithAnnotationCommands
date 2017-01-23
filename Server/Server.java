/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Command.CommandMgr;
import Server.commands.Replayer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nutzer
 */
public class Server {
    private boolean isListening;
    private ServerSocket serverSocket;
    private final int port;
    private final List<ClientThread> clientThreads;
    private final Executor executorService;
    private final CommandMgr commandMgr;
    
    public Server(int port) {
        this.isListening = false;
        this.port = port;
        this.clientThreads = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(2); // 2 clients accepted since it's TicTacToe
        this.commandMgr = new CommandMgr();
        this.commandMgr.loadCommands(Replayer.class);
    }
    
    /**
     * Listen on incoming connections, this method create our ServerSocket, and calls
     * the method that will take care of incoming connections.
     * 
     * Note that incoming connections are handled on a separate thread!
     */
    public void start() {
        openServerSocket();
        waitForConnections();
    }
    
    public void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);  
            this.isListening = true;
        } catch (IOException ex) {
            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Could not bind on port " + this.port);
        }
    }
    
    /**
     * Wait for incoming connections, once a client enters, accept it and add it to our pool of clients
     * This happens on a background thread to make sure that our main thread is safe.
     */
    public void waitForConnections() {
        while (isListening) {
            Socket client = null;
            
            // Accept new clients
            try {
                client = this.serverSocket.accept();
            } catch (IOException ex) {
                Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Once a client connected, put it on a new thread
            System.out.println("[Server] Socket connected, putting it on a new thread");

            ClientThread clientThread = new ClientThread(client);
            clientThread.start();
            clientThreads.add(clientThread);
        }
    }
    
    public void removeThread(ClientThread t) {
        clientThreads.remove(t);
    }
    
    public class ClientThread extends Thread {
        private final Socket socket;

        ClientThread(Socket acceptedSocket) {
            this.socket = acceptedSocket;
        }

        @Override
        public void run() {
            System.out.println("[Server] Listening for messages from " + this.socket.getLocalSocketAddress());

            try {
                DataInputStream in = new DataInputStream(this.socket.getInputStream());
                
                while (this.socket.isConnected()) {
                    String message = in.readUTF();
                    
                    // Parse the image and load the handler for it
                    commandMgr.execute(this.socket, message);
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
                removeThread(this);
                System.out.println("Socket disconnected");
            }
        }
    }
}
package utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderUtil {
    /**
     * Send a message to a given socket
     * Note: Do not call out.close since this will close the socket connection
     * @param socket
     * @param message 
     */
    public static void sendMessage(Socket socket, String message) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
            out.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

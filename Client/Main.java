package Client;

import java.util.Scanner;
import utils.SenderUtil;

public class Main {
    public static void main(String[] args) {
        // Init connection to server
        Client client = new Client("127.0.0.1", 8000);
        
        // Read from stdin and send to server
        Scanner in = new Scanner(System.in);
        
        // Read input messages from console
        while (true) {
            String message = in.nextLine();
            System.out.println("[Client] Sending message: " + message);
            SenderUtil.sendMessage(client.getSocket(), message);
        }
    }
}

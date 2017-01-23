package Client.commands;

import Command.Command;

public class Replayer {
    /**
     * [0] = clientSocket
     * [1] = message
     * @param args 
     */
    @Command(name="SMSG_REPLAY", description="The replayed message from the server")
    public void handleClientInfo(Object[] args) {
        System.out.println("Received message from server: " + (String)args[1]);
    }
}

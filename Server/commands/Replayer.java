/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.commands;

import Command.Command;
import java.net.Socket;
import utils.SenderUtil;

public class Replayer {
    /**
     * [0] = clientSocket
     * [1] = message
     * @param args 
     */
    @Command(name="CMSG_REPLAY", description="Replays a message to the client")
    public void handleClientInfo(Object[] args) {
        SenderUtil.sendMessage((Socket)args[0], "SMSG_REPLAY " + (String)args[1]);
    }
}

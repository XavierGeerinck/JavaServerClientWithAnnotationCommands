package Command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandMgr {
    private final HashMap<String, CommandObj> commands;

    public CommandMgr() {
        this.commands = new HashMap<>();
    }
    
    /**
     * Load the commands from a class
     * @param c 
     */
    public void loadCommands(Class c) {
        // Check the annotation in all the methods.
        for (Method method : c.getDeclaredMethods()) {
            // Get the annotations for that method
            Command annotation = method.getAnnotation(Command.class);

            if (annotation != null) {
                System.out.println("[CommandMgr]: Registered Command " + annotation.name());
                commands.put(annotation.name(), new CommandObj(c, annotation, method));
            } 
        }
    }

    /**
     * Gets the description for a loaded command
     * @param command
     * @return 
     */ 
    public String getDescription(String command) {
        return commands.get(command).getAnnotationDescription();
    }

    /**
     * Parses the command and executes it
     * @param command 
     */
    public void execute(Socket clientSocket, String command) {
        String parsed[] = command.split(" ");
        String commandKey = parsed[0];
        String argsParsed[] = Arrays.copyOfRange(parsed, 1, parsed.length);
        
        // Convert args to linkedlist
        LinkedList<Object> args = new LinkedList();
        
        for (Object arg : argsParsed) {
            args.addLast(arg);
        }
        
        args.addFirst(clientSocket); // Add socket to args at front
        
        if (command.length() == 0) {
            return;
        }
        
        if (!commands.containsKey(commandKey)) {
            Logger.getLogger(CommandMgr.class.getName()).log(Level.SEVERE, null, "Count not find the correct handler for command: " + commandKey);
            return;
        }

        // Get the command to execute
        CommandObj c = commands.get(commandKey);
       
        // Execute the method
        try {
            c.getMethod().invoke(c.getClassToInvoke().newInstance(), (Object)args.toArray());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException ex) {
            Logger.getLogger(CommandMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CommandMgr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(CommandMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

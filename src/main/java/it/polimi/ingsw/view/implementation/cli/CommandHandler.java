package it.polimi.ingsw.view.implementation.cli;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandHandler {
    private final CLI cli;

    public CommandHandler(CLI cli) {
        this.cli = cli;
    }

    public void handle(String command) throws IllegalArgumentException {
        if(command == null)
            throw new IllegalArgumentException("Command can't be null");
        if(command.trim().equals(""))
            throw new IllegalArgumentException("Command can't be empty");

        String[] split = command.split(" ");
        String cmd = split[0];
        String[] args = new String[0];
        if(split.length > 1)
            args = Arrays.copyOfRange(split, 1, split.length);

        try {
            Method cmdHandler = getClass().getMethod(cmd, args.getClass());
            cmdHandler.invoke(this, (Object) args);
        } catch (NoSuchMethodException | SecurityException |
                IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Command does not exists.");
        }
    }

    public void test(String[] args) {
        System.out.println("Received cmd Test with args: " + Arrays.toString(args));
    }
}

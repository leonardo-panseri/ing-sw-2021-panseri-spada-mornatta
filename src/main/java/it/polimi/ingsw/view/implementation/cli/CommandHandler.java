package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.model.Resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

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
        if (cmd.equals("view")) {
            split[1] = split[1].substring(0, 1).toUpperCase(Locale.ROOT) + split[1].substring(1);
            cmd = cmd.concat(split[1]);
            args = null;
        }
        else if(split.length > 1)
            args = Arrays.copyOfRange(split, 1, split.length);

        try {
            Method cmdHandler;
            if(args != null) {
                cmdHandler = getClass().getMethod(cmd, args.getClass());
                cmdHandler.invoke(this, (Object) args);
            }
            else {
                cmdHandler = getClass().getMethod(cmd);
                cmdHandler.invoke(this);
            }
        } catch (NoSuchMethodException | SecurityException |
                IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Command does not exists.");
        }
    }

    public void viewLeaders() {
        cli.printOwnLeaders();
    }

    public void viewDevelopment() {
        cli.printOwnDevelopmentCards();
    }

    public void viewDeck() {
        cli.printDevelopmentDeck();
    }

    public void viewDeposit() {
        cli.printDeposit();
    }

    public void viewMarket() {
        cli.printMarket();
    }

    public void viewFaith(){};

    public void buy(String[] args) {
        int cardIndex;
        try{
            cardIndex = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            System.out.println("Incorrect format: please input \"buy\" <cardNum>");
            return;
        }
        cli.buyDevelopmentCard(cardIndex);
    }

    public void draw(String[] args) {
        int marketIndex;
        Resource whiteConversion = null;
        try{
            marketIndex = Integer.parseInt(args[0]);
            if (marketIndex< 1 || marketIndex > 7) throw new IllegalArgumentException("incorrect_format");
            if(args.length>1) {
                whiteConversion = Resource.valueOf(args[1]);
            }
        }catch (IllegalArgumentException e){
            System.out.println("Incorrect format: please input \"draw\" <rowNum or columnNum> <resource to take instead of white spheres>");
            return;
        }
        cli.draw(marketIndex, whiteConversion);
    }

    public void test(String[] args) {
        System.out.println("Received cmd Test with args: " + Arrays.toString(args));
    }
}

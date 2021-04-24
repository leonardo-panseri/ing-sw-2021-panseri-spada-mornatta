package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        cli.getRenderer().printOwnLeaders();
    }

    public void viewDevelopment() {
        cli.getRenderer().printOwnDevelopmentCards();
    }

    public void viewDeck() {
        cli.getRenderer().printDevelopmentDeck();
    }

    public void viewDeposit() {
        cli.getRenderer().printOwnDeposit();
    }

    public void viewStrongbox() {cli.getRenderer().printOwnStrongbox();};

    public void viewMarket() {
        cli.getRenderer().printMarket();
    }

    public void viewResult() {
        cli.getRenderer().printMarketResult();
    }

    public void viewFaith(){cli.getRenderer().printFaith(cli.getModel().getFaithPoints());}

    public void buy(String[] args) {
        if(cli.hasAlreadyPlayed()) {
            cli.getRenderer().showErrorMessage(ViewString.ALREADY_PLAYED);
            return;
        }
        int cardIndex;
        try{
            cardIndex = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.BUY_CARD);
            return;
        }
        cli.getActionSender().buyDevelopmentCard(cardIndex);
    }

    public void draw(String[] args) {
        if(cli.hasAlreadyPlayed()) {
            cli.getRenderer().showErrorMessage(ViewString.ALREADY_PLAYED);
            return;
        }
        int marketIndex;
        try {
            marketIndex = Integer.parseInt(args[0]);
            if (marketIndex< 1 || marketIndex > 7) throw new IllegalArgumentException("incorrect_format");
        } catch (IllegalArgumentException e){
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.DRAW_MARKET);
            return;
        }
        int whiteResources = cli.getModel().countWhiteResources(marketIndex);
        List<Resource> whiteConversions = new ArrayList<>();
        for(int i = 1; i < args.length; i++) {
            if(whiteConversions.size() <= whiteResources) {
                try {
                    Resource res = Resource.valueOf(args[i].toUpperCase());
                    whiteConversions.add(res);
                } catch (IllegalArgumentException e) {
                    cli.getRenderer().showErrorMessage(args[i] + " is not a valid resource");
                }
            }
        }
        cli.getActionSender().draw(marketIndex, whiteConversions);
    }

    public void spy(String[] args) {
        if(args.length < 2) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.SPY);
        }
        String playerName = args[0];
        String object = args[1];

        switch (object) {
            case "leaders" -> cli.getRenderer().printOthersLeaderCards(playerName);
            case "development" -> cli.getRenderer().printOthersDevelopmentCards(playerName);
            case "deposit" -> cli.getRenderer().printOthersDeposit(playerName);
            case "faith" -> cli.getRenderer().printOthersFaith(playerName);
        }
    }

    public void discard(String[] args) {
        if(args.length < 1) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.DISCARD);
        }
        int index;
        try{
            index = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.DISCARD);
            return;
        }
        if(index > cli.getModel().getLeaderCards().size()) {
            cli.getRenderer().showErrorMessage("Index out of bound");
            return;
        }
        cli.getActionSender().discard(index);
    }

    public void move(String[] args) {
        if(args.length < 2) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.MOVE_DEPOSIT);
        }
        int[] index = new int[2] ;
        for(int j = 0; j < 2; j++){
            try{
                index[j] = Integer.parseInt(args[j]);
            }catch (NumberFormatException e){
                System.out.println(ViewString.INCORRECT_FORMAT + ViewString.MOVE_DEPOSIT);
                return;
            }
            if(index[j] < 1 || index[j] > 3) {
                cli.getRenderer().showErrorMessage("Index out of bound");
                return;
            }
        }
        cli.getActionSender().move(index[0], index[1]);
    }

    public void store(String[] args) {
        if(args.length < 2) {
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.STORE_DEPOSIT);
        }
        int[] index = new int[2] ;
        for(int j = 0; j < 2; j++){
            try{
                index[j] = Integer.parseInt(args[j]);
            }catch (NumberFormatException e){
                System.out.println(ViewString.INCORRECT_FORMAT + ViewString.STORE_DEPOSIT);
                return;
            }
        }
        if(index[0] < 1 || index[0] > cli.getModel().getMarketResult().size() ) {
            cli.getRenderer().showErrorMessage("Market index out of bound");
            return;
        }
        if(index[1] < 1 || index[1] > 3 ) {
            cli.getRenderer().showErrorMessage("Row index out of bound");
            return;
        }

        cli.getActionSender().storeMarketResult(index[0], index[1]);
    }

    public void endturn(String[] args) {
        cli.getActionSender().endTurn();
    }

    public void activate(String[] args){
        if(args.length < 1){
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.ACTIVATE_LEADER);
        }
        int index = 0;
        try{
            index = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            System.out.println(ViewString.INCORRECT_FORMAT + ViewString.ACTIVATE_LEADER);
            return;
        }
        if(index > cli.getModel().getLeaderCards().size()) {
            cli.getRenderer().showErrorMessage("Index out of bound");
            return;
        }
        cli.getActionSender().setActive(index);
    }

    public void help(String[] args) {
        cli.getRenderer().help();
    }

    public void production(String[] args) {
        if(cli.hasAlreadyPlayed() && !cli.isUsingProductions()) {
            cli.getRenderer().showErrorMessage(ViewString.ALREADY_PLAYED);
            return;
        }
        if(args.length < 1) {
            cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_PRODUCTION);
            return;
        }
        switch (args[0]) {
            case "leader" -> {
                if(args.length != 3) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_PRODUCTION);
                    return;
                }
                int index = 0;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_PRODUCTION);
                    return;
                }
                if(index > 2 || index < 1) {
                    cli.getRenderer().showErrorMessage("Index out of bounds");
                    return;
                }
                Resource toReceive = null;
                try {
                    toReceive = Resource.valueOf(args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    cli.getRenderer().showErrorMessage("Not a valid resource");
                    return;
                }

                cli.getActionSender().useLeaderProduction(index, toReceive);
            }
            case "development" -> {
                if(args.length != 2) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_PRODUCTION);
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_PRODUCTION);
                    return;
                }
                if(index > 3 || index < 1) {
                    cli.getRenderer().showErrorMessage("Index out of bounds");
                    return;
                }

                cli.getActionSender().useDevelopmentProduction(index);
            }
            case "base" -> {
                if(args.length != 4) {
                    cli.getRenderer().showErrorMessage(ViewString.INCORRECT_FORMAT + ViewString.USE_PRODUCTION);
                    return;
                }
                List<Resource> inputResources = new ArrayList<>();
                for(int i = 1; i < 3; i++) {
                    try {
                        Resource resource = Resource.valueOf(args[i].toUpperCase());
                        inputResources.add(resource);
                    } catch (IllegalArgumentException e) {
                        cli.getRenderer().showErrorMessage("Input resource " + i + " is not a valid resource");
                        return;
                    }
                }
                Resource outputResource;
                try {
                    outputResource = Resource.valueOf(args[3].toUpperCase());
                } catch (IllegalArgumentException e) {
                    cli.getRenderer().showErrorMessage("Output resource is not a valid resource");
                    return;
                }

                cli.getActionSender().useBaseProduction(inputResources, outputResource);
            }
        }
    }

    public void execute(String[] args) {
        if(!cli.isUsingProductions()) {
            cli.getRenderer().showErrorMessage("You are not using productions");
            return;
        }
        cli.getActionSender().executeProductions();
    }
}

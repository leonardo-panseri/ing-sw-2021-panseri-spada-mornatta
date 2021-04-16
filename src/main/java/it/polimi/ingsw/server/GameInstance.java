package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.player.Player;

public class GameInstance extends Thread {
    private final Lobby lobby;

    GameInstance(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void run() {
        System.out.println("Starting game!");

        GameController controller = new GameController();
        for(SocketClientConnection conn : lobby.getConnections()) {
            conn.setLobbyUUID(lobby.getUuid());

            Player player = new Player(conn.getPlayerName());
            controller.addPlayer(player);
            RemoteView remoteView = conn.getRemoteView();
            remoteView.setPlayer(player);
            remoteView.setGameController(controller);
            controller.getGame().addObserver(remoteView);
            controller.getGame().getDeck().addObserver(remoteView);
            controller.getGame().getMarket().addObserver(remoteView);
            player.addObserver(remoteView);
            player.getBoard().addObserver(remoteView);
            player.getBoard().getDeposit().addObserver(remoteView);

            if(lobby.isSinglePlayer())
                controller.getGame().createLorenzo().addObserver(remoteView);
        }

        lobby.startGame();

        controller.getGame().getMarket().initializeMarket();
        controller.getGame().getDeck().shuffleDevelopmentDeck();
        controller.getTurnController().start();
    }
}
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.player.Player;

public class BoughtCardUpdate extends PropertyUpdate{
    Player player;
    DevelopmentCard card;
}

package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.util.Map;

/**
 * Subclass that models a leader card.
 */
public class LeaderCard extends Card {
    @Serial
    private static final long serialVersionUID = -7287359739334056170L;

    private LeaderCardRequirement cardRequirements;
    private SpecialAbility specialAbility;

    /**
     * Default constructor for correct GSON deserialization
     */
    public LeaderCard() {
        super(-1);
    }

    /**
     * Constructor for a new LeaderCard.
     *
     * @param victoryPoints the amount of victory points that this card will give
     * @param cardRequirements a leadercardrequirement object that stores the requirements, resources or cards, needed to activate the leader.
     * @param specialAbility the special ability granted by the card upon activation.
     */
    public LeaderCard(int victoryPoints, LeaderCardRequirement cardRequirements, SpecialAbility specialAbility) {
        super(victoryPoints);
        this.cardRequirements = cardRequirements;
        this.specialAbility = specialAbility;
    }

    @Override
    public boolean canPlayerAfford(Player player) {
        return cardRequirements.canPlayerAfford(player);
    }

    /**
     * Getter for the requirements.
     *
     * @return the card requirements.
     */
    public LeaderCardRequirement getCardRequirements() {
        return cardRequirements;
    }

    /**
     * Getter for the special ability.
     *
     * @return the special ability
     */
    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    @Override
    public String toString() {
        return "LeaderCard{" +
                "uuid=" + getUuid() +
                ", victoryPoints=" + getVictoryPoints() +
                ", cardRequirements=" + cardRequirements +
                ", specialAbility=" + specialAbility +
                '}';
    }
}

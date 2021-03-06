package it.polimi.ingsw.server;

import com.google.gson.*;
import it.polimi.ingsw.model.BaseProductionPower;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Class representing a game configuration that will be used to play the game.
 */
public class GameConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 5450385480341924714L;
    private static final String DEFAULT_GAMECONFIG_PATH = "default_gameconfig.json";

    /**
     * Map correlating pope report slot in the faith track with:
     * <ol><li>victory points awarded to players in range</li><li>range of the pope report</li></ol>
     */
    private Map<Integer, List<Integer>> popeReports;
    private Map<Integer, Integer> faithTrackPoints;
    private BaseProductionPower baseProductionPower;
    private List<LeaderCard> leaderCards;
    private List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards;

    public Map<Integer, List<Integer>> getPopeReports() {
        return popeReports;
    }

    public Map<Integer, Integer> getFaithTrackPoints() {
        return faithTrackPoints;
    }

    public BaseProductionPower getBaseProductionPower() {
        return baseProductionPower;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public List<HashMap<CardColor, Stack<DevelopmentCard>>> getDevelopmentCards() {
        return developmentCards;
    }

    /**
     * Serializes the given GameConfig to a String.
     *
     * @param gameConfig the game config to serialize
     * @return a string representing the given game config
     */
    public static String serialize(GameConfig gameConfig) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getDeclaringClass() == Card.class
                        && fieldAttributes.getName().equals("uuid");
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        });
        Gson gson = builder.create();
        return gson.toJson(gameConfig);
    }

    /**
     * Deserializes the given serialized GameConfig.
     *
     * @param serializedGameConfig a string representing a game config
     * @return the deserialized game config
     */
    public static GameConfig deserialize(String serializedGameConfig) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(serializedGameConfig, GameConfig.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Loads the default game config.
     *
     * @return the default game config
     */
    public static GameConfig loadDefaultGameConfig() {
        InputStream in = GameConfig.class.getClassLoader().getResourceAsStream(DEFAULT_GAMECONFIG_PATH);
        if (in == null) {
            System.err.println("Can't open default game config!");
            return null;
        }
        try {
            byte[] encoded = in.readAllBytes();
            String serializedGameConfig = new String(encoded, StandardCharsets.UTF_8);
            return deserialize(serializedGameConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void modifyLeaderCard(LeaderCard oldCard, LeaderCard newCard) {
        leaderCards.set(getLeaderCards().indexOf(oldCard), newCard);
    }

    public void addNewLeaderCard(LeaderCard card) {
        getLeaderCards().add(card);
    }

    public void deleteLeaderCard(LeaderCard card) {
        getLeaderCards().remove(card);
    }

    public void modifyBaseProduction(BaseProductionPower modifiedBaseProduction) {
        baseProductionPower = modifiedBaseProduction;
    }

    public void modifyDevelopmentCard(DevelopmentCard oldCard, DevelopmentCard newCard) {
        int level = oldCard.getLevel();
        CardColor color = oldCard.getColor();
        Stack<DevelopmentCard> pile = developmentCards.get(level - 1).get(color);
        pile.set(pile.indexOf(oldCard), newCard);
    }

    public void modifyFaithTrack(Map<Integer, List<Integer>> newPopeReports, Map<Integer, Integer> newVictoryPoints) {
        popeReports = newPopeReports;
        faithTrackPoints = newVictoryPoints;
    }
}
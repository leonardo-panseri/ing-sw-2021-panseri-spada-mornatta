package it.polimi.ingsw.constant;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Utility class for parsing card objects from JSON files.
 */
public class DeckParser {
    private static final String LEADER_CARDS_FILE = "leader_cards.json";
    private static final String DEVELOPMENT_CARDS_FILE = "development_cards.json";

    /**
     * Creates all {@link LeaderCard} objects from a configuration file and returns them in a List.
     *
     * @return a list of the leader cards parsed from the file, any error during parsing will cause the method to return an empty list
     */
    public static List<LeaderCard> loadLeaderCards() {
        List<LeaderCard> leaderCards = new ArrayList<>();

        JsonElement parsedJson = parseJsonFromFile(LEADER_CARDS_FILE);
        if(parsedJson == null) {
            return leaderCards;
        }
        JsonArray array = parsedJson.getAsJsonArray();

        if(array != null) {
            GsonBuilder builder = new GsonBuilder();
            //builder.registerTypeAdapter(LeaderCardRequirement.class, new LeaderCardRequirementAdapter());
            Gson gson = builder.create();
            try {
                for(JsonElement serializedCard : array) {
                    LeaderCard leaderCard = gson.fromJson(serializedCard, LeaderCard.class);
                    leaderCards.add(leaderCard);
                }
            } catch (JsonSyntaxException e) {
                System.out.println("File " + LEADER_CARDS_FILE + " has incorrect syntax");
            }
        }

        return leaderCards;
    }

    /**
     * Creates all {@link DevelopmentCard} objects from a configuration file and returns them in a List.
     *
     * @return a list of the development cards parsed from the file, any error during parsing will cause the list to be empty
     */
    public static List<HashMap<CardColor, Stack<DevelopmentCard>>> loadDevelopmentCards() {
        List<HashMap<CardColor, Stack<DevelopmentCard>>> developmentCards = new ArrayList<>();

        JsonElement parsedJson = parseJsonFromFile(DEVELOPMENT_CARDS_FILE);
        if(parsedJson == null) {
            return developmentCards;
        }
        
        JsonObject object = parsedJson.getAsJsonObject();
        developmentCards.add(parseDevelopmentCardsOfLevel(object, "levelOne"));
        developmentCards.add(parseDevelopmentCardsOfLevel(object, "levelTwo"));
        developmentCards.add(parseDevelopmentCardsOfLevel(object, "levelThree"));

        return developmentCards;
    }

    /**
     * Parses all the {@link DevelopmentCard} of the level identified by id from the given JsonObject.
     *
     * @param obj a json object containing the serialized development cards organized by level and color
     * @param id a string representing the identifier for the level that should be parsed
     * @return an hashmap that maps card colors to a stack of development cards
     */
    private static HashMap<CardColor, Stack<DevelopmentCard>> parseDevelopmentCardsOfLevel(JsonObject obj, String id) {
        HashMap<CardColor, Stack<DevelopmentCard>> result = new HashMap<>();

        JsonObject colors = obj.get(id).getAsJsonObject();
        result.put(CardColor.GREEN, parseDevelopmentCardsFromArray(colors.get("GREEN").getAsJsonArray()));
        result.put(CardColor.BLUE, parseDevelopmentCardsFromArray(colors.get("BLUE").getAsJsonArray()));
        result.put(CardColor.YELLOW, parseDevelopmentCardsFromArray(colors.get("YELLOW").getAsJsonArray()));
        result.put(CardColor.PURPLE, parseDevelopmentCardsFromArray(colors.get("PURPLE").getAsJsonArray()));

        return result;
    }

    /**
     * Parses all the {@link DevelopmentCard} from the given JsonArray.
     *
     * @param array a json array containing the serialized development cards
     * @return a stack of development cards parsed from the json array
     */
    private static Stack<DevelopmentCard> parseDevelopmentCardsFromArray(JsonArray array) {
        Stack<DevelopmentCard> result = new Stack<>();

        Gson gson = new Gson();
        try {
            for (JsonElement serializedCard : array) {
                DevelopmentCard developmentCard = gson.fromJson(serializedCard, DevelopmentCard.class);
                result.add(developmentCard);
            }
        } catch (JsonSyntaxException e) {
            System.out.println("File " + DEVELOPMENT_CARDS_FILE + " has incorrect syntax");
        }

        return result;
    }

    /**
     * Parses a JsonElement from a file in the resources folder with the given path.
     *
     * @param fileName the path to the file relative to the resources folder
     * @return a json element representing the content of the file
     */
    private static JsonElement parseJsonFromFile(String fileName) {
        InputStreamReader reader;
        InputStream in = DeckParser.class.getClassLoader().getResourceAsStream(fileName);
        if(in != null) {
            reader = new InputStreamReader(in);
        } else {
            System.out.println("Can't open file " + fileName);
            return null;
        }

        JsonElement result = null;
        try {
            result = JsonParser.parseReader(reader);
        } catch (JsonIOException | JsonSyntaxException e) {
            System.out.println("Can't read from file " + fileName);
            e.printStackTrace();
        }
        return result;
    }

    /*/**
     * Adapter for deserialization of {@link LeaderCardRequirement} from a json object.
     *
     * @see com.google.gson.TypeAdapter
     *\/
    private static class LeaderCardRequirementAdapter extends TypeAdapter<LeaderCardRequirement> {

        @Override
        public void write(JsonWriter jsonWriter, LeaderCardRequirement leaderCardRequirement) throws IOException {
            jsonWriter.value(leaderCardRequirement.toString());
        }

        @Override
        public LeaderCardRequirement read(JsonReader jsonReader) throws IOException {
            String enumConstant = jsonReader.nextString();
            CardColor color;
            try {
                color = CardColor.valueOf(enumConstant);
                return color;
            } catch (IllegalArgumentException ignored) {}
            Resource resource;
            try {
                resource = Resource.valueOf(enumConstant);
                return resource;
            } catch (IllegalArgumentException ignored) {}
            return null;
        }
    }*/
}

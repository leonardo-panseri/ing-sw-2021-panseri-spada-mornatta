package it.polimi.ingsw.constant;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeckParser {
    private static final String LEADER_CARDS_FILE = "leader_cards.json";
    private static final String DEVELOPMENT_CARDS_FILE = "development_cards.json";

    public static List<LeaderCard> loadLeaderCards() {
        List<LeaderCard> leaderCards = new ArrayList<>();

        JsonArray array = parseJsonArrayFromFile(LEADER_CARDS_FILE);
        if(array != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LeaderCardRequirement.class, new LeaderCardRequirementAdapter());
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

    public static List<DevelopmentCard> loadDevelopmentCards() {
        List<DevelopmentCard> developmentCards = new ArrayList<>();

        JsonArray array = parseJsonArrayFromFile(DEVELOPMENT_CARDS_FILE);
        if(array != null) {
            Gson gson = new Gson();
            try {
                for (JsonElement serializedCard : array) {
                    DevelopmentCard developmentCard = gson.fromJson(serializedCard, DevelopmentCard.class);
                    developmentCards.add(developmentCard);
                }
            } catch (JsonSyntaxException e) {
                System.out.println("File " + DEVELOPMENT_CARDS_FILE + " has incorrect syntax");
            }
        }
        return developmentCards;
    }

    private static JsonArray parseJsonArrayFromFile(String fileName) {
        InputStreamReader reader;
        InputStream in = DeckParser.class.getClassLoader().getResourceAsStream(fileName);
        if(in != null) {
            reader = new InputStreamReader(in);
        } else {
            System.out.println("Can't open file " + fileName);
            return null;
        }

        JsonParser parser = new JsonParser();
        return parser.parse(reader).getAsJsonArray();
    }

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
    }
}

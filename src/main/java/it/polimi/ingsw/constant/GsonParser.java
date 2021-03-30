package it.polimi.ingsw.constant;

import com.google.gson.Gson;

public class GsonParser {
    private static GsonParser instance;
    private Gson gson;

    private GsonParser() {
        gson = new Gson();
    }

    public static GsonParser instance() {
        if(instance == null) {
            instance = new GsonParser();
        }
        return instance;
    }

    public Gson getGson() {
        return gson;
    }
}

package me.ericjiang.settlers.spark;

import java.util.HashMap;

import me.ericjiang.settlers.core.Lobby;
import me.ericjiang.settlers.spark.auth.Authenticator;

import spark.Request;

public class LobbyModel extends HashMap<String, Object> {

    public LobbyModel(Lobby lobby, Request request) {
        String userId = request.session().attribute(Authenticator.USER_ID);
        put(Authenticator.USER_ID, userId);
        put("gamesForPlayer", lobby.gamesForPlayer(userId));
        put("openGames", lobby.openGames());
    }
}

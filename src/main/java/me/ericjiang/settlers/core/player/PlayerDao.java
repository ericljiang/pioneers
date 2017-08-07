package me.ericjiang.settlers.core.player;

import java.util.List;

public interface PlayerDao {

    List<String> playersForGame(String gameId);

    void addPlayerToGame(String gameId, String playerId);
    
    String getName(String playerId);

    void setName(String playerId, String name);

}
package me.ericjiang.settlers.data.player;

import java.util.Map;
import me.ericjiang.settlers.core.game.Game.Color;

public interface PlayerDao {

    /**
     * Returns a list of players in a game, in turn order
     */
    Map<Color, String> playersForGame(String gameId);

    void addPlayerToGame(String gameId, String playerId, Color color, int position);

    void removePlayerFromGame(String gameId, String playerId);

    String getName(String playerId);

    void setName(String playerId, String name);

}

package me.ericjiang.settlers.data.game;

import java.util.List;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.core.game.Game.Phase;

public interface GameDao {

    Game createGame(String name, String expansion);

    List<Game> gamesForPlayer(String playerId);

    List<Game> openGames();

    List<Game> openGamesWithoutPlayer(String playerId);

    Game getGame(String id);

    Phase getPhase(String gameId);

    void setPhase(String gameId, Phase phase);

    Color getActivePlayer(String gameId);

    void setActivePlayer(String gameId, Color color);

}

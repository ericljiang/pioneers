package me.ericjiang.settlers.core.game;

import java.util.List;
import me.ericjiang.settlers.core.game.Game;

public interface GameDao {

    void createGame(String name, String expansion);

    List<Game> gamesForPlayer(String playerId);

    List<Game> openGames();

    List<Game> openGamesWithoutPlayer(String playerId);

    Game getGame(String id);

}

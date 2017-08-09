package me.ericjiang.settlers.data.game;

import java.util.List;
import me.ericjiang.settlers.core.game.Game;

public interface GameDao {

    Game createGame(String name, String expansion);

    List<Game> gamesForPlayer(String playerId);

    List<Game> openGames();

    List<Game> openGamesWithoutPlayer(String playerId);

    Game getGame(String id);

}

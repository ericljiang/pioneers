package me.ericjiang.settlers.core.game;

import java.util.List;
import me.ericjiang.settlers.core.board.BoardDao;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.player.PlayerDao;

public interface GameDao {
    
    void createGame(String name, String expansion, BoardDao boardDao, PlayerDao playerDao);

    List<Game> gamesForPlayer(String playerId);

    List<Game> openGames();

    List<Game> openGamesWithoutPlayer(String playerId);

    Game getGame(String id);

}
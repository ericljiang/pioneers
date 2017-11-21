package me.ericjiang.settlers.library.data;

import java.util.Map;
import me.ericjiang.settlers.library.Game;

public interface GameDao {

    Map<String, Game> loadGames();

    String getNewId();

    void save(Game game);

}

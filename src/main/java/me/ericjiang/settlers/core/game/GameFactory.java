package me.ericjiang.settlers.core.game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import me.ericjiang.settlers.core.board.BoardDao;
import me.ericjiang.settlers.core.player.PlayerDao;
import me.ericjiang.settlers.util.ShortUUID;

public class GameFactory {

    public static final String BASE = "Base";

    public static final String EXTENDED = "Extended";

    private static final Map<String, GameConstructor> EXPANSIONS;

    static {
        EXPANSIONS = new LinkedHashMap<String, GameConstructor>();
        EXPANSIONS.put(BASE, BaseGame::new);
        EXPANSIONS.put(EXTENDED, ExtendedGame::new);
    }

    public static Game newGame(String name, String expansion, BoardDao boardDao, PlayerDao playerDao) {
        String id = ShortUUID.randomUUID().toString();
        Game game = EXPANSIONS.get(expansion).instantiate(id, name);
        // TODO:
        game.setBoardDao(boardDao);
        game.setPlayerDao(playerDao);
        return game;
    }

    public static Set<String> expansions() {
        return EXPANSIONS.keySet();
    }

    private interface GameConstructor {
        Game instantiate(String id, String name);
    }

}
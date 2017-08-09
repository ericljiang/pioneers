package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
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

    /**
     * Create a new Game instance.
     */
    public static Game newGame(String name, String expansion) {
        String id = ShortUUID.randomUUID().toString();
        LocalDateTime creationTime = LocalDateTime.now();
        Game game = loadGame(id, creationTime, name, expansion);
        return game;
    }

    /**
     * Create a Game instance corresponding to a previously persisted game.
     * @param creationTime
     */
    public static Game loadGame(String id, LocalDateTime creationTime, String name, String expansion) {
        Game game = EXPANSIONS.get(expansion).instantiate(id, creationTime, name);
        return game;
    }

    public static Set<String> expansions() {
        return EXPANSIONS.keySet();
    }

    private interface GameConstructor {
        Game instantiate(String id, LocalDateTime creationTime, String name);
    }

}

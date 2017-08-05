package me.ericjiang.settlers.core.game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class GameFactory {

    public static final String BASE = "Base";

    public static final String EXTENDED = "Extended";

    private static final Map<String, GameConstructor> EXPANSIONS;

    static {
        EXPANSIONS = new LinkedHashMap<String, GameConstructor>();
        EXPANSIONS.put(BASE, BaseGame::new);
        EXPANSIONS.put(EXTENDED, ExtendedGame::new);
    }

    public static Game newGame(String name, String expansion) {
        return EXPANSIONS.get(expansion).instantiate(name);
    }

    public static Set<String> expansions() {
        return EXPANSIONS.keySet();
    }

    private interface GameConstructor {
        Game instantiate(String name);
    }

}
package me.ericjiang.settlers.library.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.library.MultiplayerModule;

@Slf4j
@Getter
public abstract class Game extends MultiplayerModule {

    private final String id;

    private final String owner;

    private final String name;

    public Game(String id, String owner, String name) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        log.info(formatLog("Game created"));
    }

    public abstract GameSummary summarize();

    @Override
    protected String getIdentifier() {
        return String.format("Game %s", id);
    }

}

package me.ericjiang.settlers.library.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.player.PlayerConnectionEvent;

@Slf4j
@Getter
public abstract class Game extends MultiplayerModule {

    private final String id;

    private final String name;

    public Game(String id, String name) {
        this.id = id;
        this.name = name;
        setEventHandlers();
    }

    public abstract GameSummary summarize();

    private void setEventHandlers() {
        on(PlayerConnectionEvent.class, e -> {
            log.info("Player " + e.getPlayerId() + " connected to Game " + id);
        });
    }

}

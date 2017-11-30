package me.ericjiang.settlers.library.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.player.PlayerConnectionEvent;
import me.ericjiang.settlers.library.player.PlayerDisconnectionEvent;

@Slf4j
@Getter
public abstract class Game extends MultiplayerModule {

    private final String id;

    private final String name;

    public Game(String id, String name) {
        this.id = id;
        this.name = name;
        setEventHandlers();
        log.info(formatLog("Game created"));
    }

    public abstract GameSummary summarize();

    private void setEventHandlers() {
        on(PlayerConnectionEvent.class, e -> {
            log.info(formatLog("Player %s connected", e.getPlayerId()));
        });
        on(PlayerDisconnectionEvent.class, e -> {
            log.info(formatLog("Player %s disconnected", e.getPlayerId()));
        });
    }

    protected String formatLog(String format, Object... args) {
        return formatLog(String.format(format, args));
    }

    protected String formatLog(String message) {
        return String.format("[Game %s] %s", id, message);
    }
}

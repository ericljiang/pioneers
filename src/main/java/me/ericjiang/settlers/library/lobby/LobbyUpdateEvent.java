package me.ericjiang.settlers.library.lobby;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.GameSummary;

@SuppressWarnings("unused")
public class LobbyUpdateEvent extends Event {

    private final Map<String, GameSummary> games;

    public LobbyUpdateEvent(Lobby<?> lobby) {
        this.games = lobby.getGames().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                                          e -> e.getValue().summarize()));
    }

}

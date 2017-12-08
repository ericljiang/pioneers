package me.ericjiang.settlers.library.lobby;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.ericjiang.settlers.library.StateEvent;
import me.ericjiang.settlers.library.game.Game;
import me.ericjiang.settlers.library.game.GameSummary;

@SuppressWarnings("unused")
public class LobbyUpdateEvent extends StateEvent {

    private final List<GameSummary> games;

    public LobbyUpdateEvent(Lobby<?> lobby) {
        this.games = lobby.getGames().values().stream()
                .map(Game::summarize)
                .collect(Collectors.toList());
    }

}

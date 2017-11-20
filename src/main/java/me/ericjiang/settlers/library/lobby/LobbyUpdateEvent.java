package me.ericjiang.settlers.library.lobby;

import java.util.List;
import java.util.stream.Collectors;

import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.GameSummary;

@SuppressWarnings("unused")
public class LobbyUpdateEvent extends Event {

    private final List<GameSummary> games;

    public LobbyUpdateEvent(Lobby lobby) {
        this.games = lobby.getAllGames().stream()
                .map(Game::summarize)
                .collect(Collectors.toList());
    }

}

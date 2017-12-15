package me.ericjiang.settlers.library.lobby;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import me.ericjiang.settlers.library.StateEvent;
import me.ericjiang.settlers.library.game.Game;
import me.ericjiang.settlers.library.game.GameSummary;

@SuppressWarnings("unused")
public class LobbyUpdateEvent extends StateEvent {

    private final List<GameSummary> games;

    public LobbyUpdateEvent(Lobby<?> lobby) {
        SortedMap<String, Game> games = lobby.getGames();
        synchronized(games) {
            this.games = games.values().stream()
                    .map(Game::summarize)
                    .collect(Collectors.toList());
        }
    }

}

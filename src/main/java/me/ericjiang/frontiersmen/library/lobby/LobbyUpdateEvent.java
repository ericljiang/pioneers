package me.ericjiang.frontiersmen.library.lobby;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import me.ericjiang.frontiersmen.library.StateEvent;
import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameSummary;

@SuppressWarnings("unused")
public class LobbyUpdateEvent extends StateEvent {

    private final List<GameSummary> games;

    public LobbyUpdateEvent(Lobby lobby) {
        this.games = lobby.getGames().descendingMap().values().stream()
                .map(Game::summarize)
                .collect(Collectors.toList());
    }

}

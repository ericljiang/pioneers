package me.ericjiang.frontiersmen.simple;

import lombok.NoArgsConstructor;
import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameSummary;
import me.ericjiang.frontiersmen.library.player.Player;

@NoArgsConstructor
public class SimpleGame extends Game {

    @SuppressWarnings("unused")
    private String testValue;

    public SimpleGame(String name, String gameId, String creatorId) {
        super(name, gameId, creatorId);
        this.testValue = "foo";
    }

    @Override
    public GameSummary summarize() {
        return new SimpleGameSummary(this);
    }

    @Override
    public int minimumPlayers() {
        return 1;
    }

    @Override
    public int maximumPlayers() {
        return 2;
    }

    @Override
    protected Player createPlayer(String playerId, int seat) {
        return new Player(playerId, seat);
    }

}

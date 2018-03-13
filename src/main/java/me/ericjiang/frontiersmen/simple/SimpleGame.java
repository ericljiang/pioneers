package me.ericjiang.frontiersmen.simple;

import lombok.NoArgsConstructor;
import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameSummary;
import me.ericjiang.frontiersmen.library.player.Player;

@NoArgsConstructor
public class SimpleGame extends Game {

    @SuppressWarnings("unused")
    private String testValue;

    public SimpleGame(String id, String owner, String name) {
        super(id, owner, name);
        this.testValue = "foo";
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
    public GameSummary summarize() {
        return new SimpleGameSummary(this);
    }

    @Override
    protected Player createPlayer(String playerId) {
        return new Player(playerId);
    }

}

package me.ericjiang.settlers.simple;

import lombok.NoArgsConstructor;
import me.ericjiang.settlers.library.game.Game;
import me.ericjiang.settlers.library.game.GameSummary;
import me.ericjiang.settlers.library.player.Player;

@NoArgsConstructor
public class SimpleGame extends Game {

    public SimpleGame(String id, String owner, String name) {
        super(id, owner, name);
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

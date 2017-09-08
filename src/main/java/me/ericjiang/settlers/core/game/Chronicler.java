package me.ericjiang.settlers.core.game;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.core.game.Game.Phase;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Chronicler {

    @NonNull
    private final String gameId;

    private Phase phase;

    private Color activePlayer;

    public void setOrder() {

    }

    public void endPhase() {

    }

    /**
     * Example:
     *
     * Chronicler chronicler = new Chronicler.Builder()
     *         .withStage(Setup, null)
     *         .withStage(FirstPlacement, FORWARD)
     *         .withPhase(BUILD)
     *         .withStage(SecondPlacement, REVERSE)
     *         .withPhase(BUILD)
     *         .withStage(MainGame, FORWARD)
     *         .withPhase(ROLL)
     *         .withPhase(TRADE)
     *         .withPhase(BUILD)
     *         .build();
     *
     * Chronicler chronicler = new Chronicler.Builder()
     *         .withStage(new Stage(null))
     *         .withStage(new Stage(FORWARD, BUILD))
     *         .withStage(new Stage(REVERSE, BUILD))
     *         .withStage(new Stage(FORWARD, ROLL, TRADE, BUILD))
     *         .build();
     */
    public class Builder {
        private final List<Object> stages;

        public Builder() {
            stages = new ArrayList<Object>();
        }

        public Builder withStage(Object stage) {
            stages.add(stage);
            return this;
        }

        public Chronicler build(String gameId) {
            return new Chronicler(gameId);
        }
    }

    public enum PlayerSequence { FORWARD, REVERSE };
}
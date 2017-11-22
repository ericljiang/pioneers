package me.ericjiang.settlers.library.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.settlers.library.MultiplayerModule;

@Getter
@AllArgsConstructor
public abstract class Game extends MultiplayerModule {

    private final String name;

    public abstract GameSummary summarize();

}

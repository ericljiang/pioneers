package me.ericjiang.settlers.library;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Game extends MultiplayerModule {

    private final String name;

    public abstract GameSummary summarize();

}

package me.ericjiang.settlers.library;

import lombok.Data;

@Data
public abstract class Game extends MultiplayerModule {

    private final String name;

}

package me.ericjiang.settlers.core.actions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameUpdate extends Action {

    private String expansion;

    private int minPlayers;

    private int maxPlayers;

}

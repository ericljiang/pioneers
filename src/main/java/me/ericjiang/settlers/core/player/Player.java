package me.ericjiang.settlers.core.player;

import me.ericjiang.settlers.core.actions.Action;

public interface Player {

    public String id();

    public void update(Action action);

}
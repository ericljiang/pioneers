package me.ericjiang.settlers.core;

import me.ericjiang.settlers.core.actions.Action;

public interface Player {
    public String id();
    public void update(Action action);
}
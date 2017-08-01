package me.ericjiang.settlers.core.actions;

import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class Action {

    private String id;

    public Action() {
        id = UUID.randomUUID().toString();
    }
}
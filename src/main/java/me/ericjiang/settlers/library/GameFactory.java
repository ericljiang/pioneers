package me.ericjiang.settlers.library;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class GameFactory {

    public abstract Game createGame(Map<String, Object> attributes);

}

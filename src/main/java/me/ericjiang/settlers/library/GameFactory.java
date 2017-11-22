package me.ericjiang.settlers.library;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class GameFactory<G extends Game> {

    public abstract G createGame(Map<String, Object> attributes);

}

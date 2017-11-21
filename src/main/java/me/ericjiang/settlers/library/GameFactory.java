package me.ericjiang.settlers.library;

import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.settlers.library.data.GameDao;

@AllArgsConstructor
public abstract class GameFactory {

    @Getter(AccessLevel.PROTECTED)
    private final GameDao gameDao;

    public abstract Game createGame(Map<String, Object> attributes);
}

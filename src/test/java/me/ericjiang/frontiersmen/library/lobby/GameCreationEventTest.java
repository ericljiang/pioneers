package me.ericjiang.frontiersmen.library.lobby;

import com.google.common.collect.Maps;

import org.junit.Test;

public class GameCreationEventTest {

    @Test
    public void shouldAllowValidName() {
        new GameCreationEvent("game", Maps.newHashMap());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowEmptyName() {
        new GameCreationEvent("", Maps.newHashMap());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowNullName() {
        new GameCreationEvent(null, Maps.newHashMap());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowLongName() {
        String name = "";
        for (int i = 0; i < GameCreationEvent.MAX_NAME_LENGTH + 1; i++) {
            name += "a";
        }
        new GameCreationEvent(name, Maps.newHashMap());
    }

}

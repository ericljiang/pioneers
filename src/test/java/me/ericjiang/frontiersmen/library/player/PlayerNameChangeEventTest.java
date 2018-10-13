package me.ericjiang.frontiersmen.library.player;

import org.junit.Test;

public class PlayerNameChangeEventTest {

    public static final String PLAYER_ID = "1";

    @Test
    public void shouldAllowValidName() {
        new PlayerNameChangeEvent(PLAYER_ID, "game");
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowEmptyName() {
        new PlayerNameChangeEvent(PLAYER_ID, "");
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowNullName() {
        new PlayerNameChangeEvent(PLAYER_ID, null);
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowLongName() {
        String name = "";
        for (int i = 0; i < PlayerNameChangeEvent.MAX_NAME_LENGTH + 1; i++) {
            name += "a";
        }
        new PlayerNameChangeEvent(PLAYER_ID, name);
    }

}

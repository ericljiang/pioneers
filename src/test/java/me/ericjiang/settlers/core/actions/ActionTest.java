package me.ericjiang.settlers.core.actions;

import static org.junit.Assert.*;

import org.junit.Test;

public class ActionTest {

    @Test
    public void shouldDeserializeWithId() {
        Action action = new ConnectAction("id", "name");
        assertNotNull(Action.valueOf(action.toString()).getId());
    }

    @Test
    public void shouldDeserializeWhatWeSerialize() {
        Action action = new ConnectAction("id", "name");
        assertEquals(action, Action.valueOf(action.toString()));
    }
}

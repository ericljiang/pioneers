package me.ericjiang.settlers.core.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import me.ericjiang.settlers.core.game.Game.Color;

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

    @Test
    public void shouldSerializeAndDeserializeColor() {
        Action joinAction = new JoinAction("id", "name", Color.BLUE);
        assertEquals(Color.BLUE, ((JoinAction) Action.valueOf(joinAction.toString())).getColor());
        Action leaveAction = new LeaveAction("id", "name", Color.BLUE);
        assertEquals(Color.BLUE, ((LeaveAction) Action.valueOf(leaveAction.toString())).getColor());
    }
}

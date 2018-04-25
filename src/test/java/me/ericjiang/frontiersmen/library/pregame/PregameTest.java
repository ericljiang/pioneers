package me.ericjiang.frontiersmen.library.pregame;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.game.Game;

public class PregameTest extends EasyMockSupport {

    private static final String PLAYER_ID = "1";

    private Game game;

    private Pregame pregame;

    @Before
    public void setup() {
        game = createNiceMock(Game.class);
        expect(game.minimumPlayers()).andReturn(0);
        expect(game.maximumPlayers()).andReturn(10);
        replay(game);
        pregame = new Pregame(game, Maps.newHashMap());
        reset(game);
    }

    @Test
    public void shouldPopulateSeat() {
        replayAll();
        TakeSeatEvent takeSeatEvent = new TakeSeatEvent(0);
        takeSeatEvent.setPlayerId(PLAYER_ID);
        LeaveSeatEvent leaveSeatEvent = new LeaveSeatEvent(0);
        takeSeatEvent.setPlayerId(PLAYER_ID);
        pregame.handleEvent(takeSeatEvent);
        assertNotNull(pregame.getPlayerSeats()[0]);
        verifyAll();
    }

    @Test
    public void shouldEvictCurrentSeatOnTakeSeat() {
        replayAll();
        TakeSeatEvent takeSeatEvent0 = new TakeSeatEvent(0);
        takeSeatEvent0.setPlayerId(PLAYER_ID);
        TakeSeatEvent takeSeatEvent1 = new TakeSeatEvent(1);
        takeSeatEvent1.setPlayerId(PLAYER_ID);
        pregame.handleEvent(takeSeatEvent0);
        pregame.handleEvent(takeSeatEvent1);
        assertNull(pregame.getPlayerSeats()[0]);
        verifyAll();
    }
}

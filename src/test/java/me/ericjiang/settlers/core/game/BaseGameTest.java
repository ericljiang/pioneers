package me.ericjiang.settlers.core.game;

import static org.easymock.EasyMock.*;

import java.time.LocalDateTime;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.settlers.core.actions.JoinAction;
import me.ericjiang.settlers.core.actions.StartAction;
import me.ericjiang.settlers.core.game.Game.Color;
import me.ericjiang.settlers.core.game.Game.Phase;
import me.ericjiang.settlers.core.player.Player;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.game.GameDao;
import me.ericjiang.settlers.data.player.PlayerDao;

public class BaseGameTest extends EasyMockSupport {

    public Game game;

    public GameDao gameDao;

    public BoardDao boardDao;

    public PlayerDao playerDao;

    @Before
    public void Before() {
        gameDao = mock(GameDao.class);
        boardDao = niceMock(BoardDao.class);
        playerDao = niceMock(PlayerDao.class);
        game = new BaseGame("0", LocalDateTime.now(), "game", gameDao, boardDao, playerDao, true);
    }

    @Test
    public void shouldChangePhaseOnStart() {
        Player player = niceMock(Player.class);
        StartAction startAction = niceMock(StartAction.class);

        expect(gameDao.getPhase("0")).andStubReturn(Phase.SETUP);
        expect(gameDao.getActivePlayer(anyString())).andStubReturn(Color.RED);
        gameDao.setPhase("0", Phase.INITIAL_PLACEMENT);
        replayAll();

        game.connectPlayer(player);
        game.handleJoinAction(new JoinAction("0", "0", Color.RED));
        game.handleJoinAction(new JoinAction("1", "1", Color.BLUE));
        game.handleJoinAction(new JoinAction("2", "2", Color.ORANGE));
        game.handleStartAction(startAction);

        Assert.assertTrue(true);
    }
}

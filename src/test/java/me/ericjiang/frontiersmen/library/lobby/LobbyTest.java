package me.ericjiang.frontiersmen.library.lobby;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Map;

import com.google.common.collect.Maps;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.game.Game;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.library.player.Player;
import me.ericjiang.frontiersmen.library.player.PlayerNameChangeEvent;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.library.pregame.Pregame;
import me.ericjiang.frontiersmen.library.pregame.StartGameEvent;

public class LobbyTest extends EasyMockSupport {

    private static final String GAME_ID = "1";
    private static final String NAME = "name";
    private static final String PLAYER_ID = "1";
    private static final Player CREATOR = new Player(PLAYER_ID);
    private static final int MINIMUM_PLAYERS = 0;
    private static final int MAXIMUM_PLAYERS = 10;
    private static final Map<String, Object> ATTRIBUTES = Maps.newHashMap();

    private GameFactory gameFactory;

    private PlayerRepository playerRepository;

    private Lobby lobby;

    @Before
    public void before() {
        gameFactory = createMock(GameFactory.class);
        expect(gameFactory.loadGames()).andStubReturn(Maps.newHashMap());
        replay(gameFactory);
        playerRepository = createMock(PlayerRepository.class);
        lobby = new Lobby(gameFactory, playerRepository);
        reset(gameFactory);
    }

    @Test
    public void testPregameAndGameCreation() {
        Game game = createNiceMock(Game.class);
        recordExpectations(game);
        replay(game);
        Pregame pregame = new Pregame(game, ATTRIBUTES);
        reset(game);

        recordExpectations(game);
        expect(gameFactory.loadGames()).andStubReturn(Maps.newHashMap());
        expect(gameFactory.createPregame(NAME, PLAYER_ID, ATTRIBUTES)).andStubReturn(pregame);
        replayAll();

        GameCreationEvent gameCreationEvent = new GameCreationEvent(NAME, ATTRIBUTES);
        gameCreationEvent.setPlayerId(PLAYER_ID);
        lobby.handleEvent(gameCreationEvent);
        assertTrue(lobby.getPregames().containsKey(GAME_ID));
        assertEquals(pregame, lobby.getPregame(GAME_ID).get());

        StartGameEvent startGameEvent = new StartGameEvent();
        startGameEvent.setPlayerId(PLAYER_ID);
        pregame.handleEvent(startGameEvent);
        assertFalse(lobby.getPregames().containsKey(GAME_ID));
        assertTrue(lobby.getGames().containsKey(GAME_ID));
        assertEquals(game, lobby.getGame(GAME_ID).get());
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowEmptyPlayerName() {
        PlayerNameChangeEvent playerNameChangeEvent = new PlayerNameChangeEvent(PLAYER_ID, "");
        lobby.handleEvent(playerNameChangeEvent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowLongPlayerName() {
        String name = "";
        for (int i = 0; i <= 32; i++) {
            name += "a";
        }
        PlayerNameChangeEvent playerNameChangeEvent = new PlayerNameChangeEvent(PLAYER_ID, name);
        lobby.handleEvent(playerNameChangeEvent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowEmptyGameName() {
        GameCreationEvent gameCreationEvent = new GameCreationEvent("", Maps.newHashMap());
        gameCreationEvent.setPlayerId(PLAYER_ID);
        lobby.handleEvent(gameCreationEvent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowLongGameName() {
        String name = "";
        for (int i = 0; i <= 32; i++) {
            name += "a";
        }
        GameCreationEvent gameCreationEvent = new GameCreationEvent(name, Maps.newHashMap());
        gameCreationEvent.setPlayerId(PLAYER_ID);
        lobby.handleEvent(gameCreationEvent);
    }

    private void recordExpectations(Game game) {
        expect(game.getName()).andStubReturn(NAME);
        expect(game.getId()).andStubReturn(GAME_ID);
        expect(game.getCreator()).andStubReturn(CREATOR);
        expect(game.minimumPlayers()).andStubReturn(MINIMUM_PLAYERS);
        expect(game.maximumPlayers()).andStubReturn(MAXIMUM_PLAYERS);
    }

}

package me.ericjiang.frontiersmen.websockets;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.common.collect.Lists;

import org.easymock.EasyMockSupport;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.MultiplayerModuleEventRouter;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

public class WebSocketTranslatorTest extends EasyMockSupport {

    private MultiplayerModuleEventRouter eventRouter;

    private PlayerRepository playerRepository;

    private WebSocketTranslator webSocketTranslator;

    private Session session;

    @Before
    public void before() {
        this.eventRouter = createNiceMock(MultiplayerModuleEventRouter.class);
        this.playerRepository = createNiceMock(PlayerRepository.class);

        expect(eventRouter.getEventTypes()).andReturn(Lists.newArrayList());
        replay(eventRouter);
        this.webSocketTranslator = new WebSocketTranslator(eventRouter, playerRepository);
        reset(eventRouter);

        this.session = createNiceMock(Session.class);

    }

    @Test
    public void shouldCloseOnGeneralSecurityException() throws IOException, GeneralSecurityException {
        eventRouter.acceptConnection(anyObject());
        expectLastCall().andThrow(new GeneralSecurityException());
        session.close(eq(WebSocketTranslator.CloseCode.AUTHENTICATION_ERROR), anyString());
        expectLastCall();
        replayAll();

        webSocketTranslator.onConnect(session);
        verifyAll();
    }
}

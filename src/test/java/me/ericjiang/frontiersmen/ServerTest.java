package me.ericjiang.frontiersmen;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;

import com.google.gson.Gson;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.Ticket;
import me.ericjiang.frontiersmen.websockets.NullWebSocketTranslator;
import me.ericjiang.frontiersmen.websockets.WebSocketTranslator;
import spark.Spark;

public class ServerTest extends EasyMockSupport {

    private static final int TEST_PORT = 8080;

    private static final String PLAYER_ID = "1";

    private static final String ID_TOKEN = "a";

    private static final Ticket AUTH_TICKET = new Ticket(PLAYER_ID);

    private WebSocketTranslator lobbyWebSocketHandler;

    private WebSocketTranslator pregameWebSocketHandler;

    private WebSocketTranslator gameWebSocketHandler;

    private Authenticator authenticator;

    private Server server;

    @Before
    public void setUp() {
        lobbyWebSocketHandler = createMock(NullWebSocketTranslator.class);
        pregameWebSocketHandler = createMock(NullWebSocketTranslator.class);
        gameWebSocketHandler = createMock(NullWebSocketTranslator.class);
        authenticator = createMock(Authenticator.class);
        server = new Server(TEST_PORT,
            lobbyWebSocketHandler,
            pregameWebSocketHandler,
            gameWebSocketHandler,
            authenticator);
        server.initialize();
        Spark.awaitInitialization();
    }

    @After
    public void tearDown() throws Exception {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    public void shouldReturnPongOnPing() throws IOException {
        replayAll();

        URL pingURL = new URL("http", "localhost", TEST_PORT, Server.PING_PATH);
        HttpURLConnection connection = (HttpURLConnection) pingURL.openConnection();

        int status = connection.getResponseCode();
        assertThat(status, is(200));

        String response = getResponse(connection);
        assertThat(response, is("pong"));

        connection.disconnect();
        verifyAll();
    }

    @Test
    public void shouldReturnAuthTicket() throws IOException, GeneralSecurityException, URISyntaxException {
        expect(authenticator.getTicket(eq(PLAYER_ID), eq(ID_TOKEN))).andReturn(AUTH_TICKET);
        replayAll();

        URL ticketURL = new URI("http",
                null, "localhost", 8080,
                Server.AUTH_TICKET_PATH,
                String.format("playerId=%s&idToken=%s", PLAYER_ID, ID_TOKEN),
                null).toURL();
        HttpURLConnection connection = (HttpURLConnection) ticketURL.openConnection();

        int status = connection.getResponseCode();
        assertThat(status, is(200));

        String response = getResponse(connection);
        assertThat(response, is(new Gson().toJson(AUTH_TICKET)));

        connection.disconnect();
        verifyAll();
    }

    @Test
    public void shouldDenyAuthTicket() throws IOException, GeneralSecurityException, URISyntaxException {
        expect(authenticator.getTicket(eq(PLAYER_ID), eq(ID_TOKEN))).andThrow(new GeneralSecurityException());
        replayAll();

        URL ticketURL = new URI("http",
                null, "localhost", 8080,
                Server.AUTH_TICKET_PATH,
                String.format("playerId=%s&idToken=%s", PLAYER_ID, ID_TOKEN),
                null).toURL();
        System.out.println(ticketURL.toString());
        HttpURLConnection connection = (HttpURLConnection) ticketURL.openConnection();

        int status = connection.getResponseCode();
        assertThat(status, is(403));

        String response = getResponse(connection);
        assertNull(response);

        connection.disconnect();
        verifyAll();
    }

    private String getResponse(HttpURLConnection connection) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (IOException e) {
            return null;
        }
    }

}

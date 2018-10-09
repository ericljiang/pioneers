package me.ericjiang.frontiersmen.library.player;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.auth.Ticket;

public class PlayerRepositoryPostgresTest {

    private static final String PLAYER_ID = "1";

    private static final String DISPLAY_NAME = "alice";

    private static final Ticket AUTH_TICKET =  new Ticket(PLAYER_ID);

    private Connection connection;

    private PlayerRepository playerRepository;

    @Before
    public void before() throws SQLException {
        final String url = "jdbc:h2:mem:test";
        final String init = "CREATE domain IF NOT EXISTS jsonb AS other\\;RUNSCRIPT FROM 'src/main/sql/create.sql'";
        connection = DriverManager.getConnection(String.format("%s;INIT=%s", url, init));
        playerRepository = new PlayerRepositoryPostgres(connection);
    }

    @Test
    public void shouldAddPlayer() throws SQLException {
        playerRepository.addPlayer(PLAYER_ID, DISPLAY_NAME, AUTH_TICKET);
        String query = "SELECT * FROM player WHERE player_id = " + PLAYER_ID;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            assertThat(resultSet.next(), is(true));
            assertThat(resultSet.getString(1), is(PLAYER_ID));
            assertThat(resultSet.getString(2), is(DISPLAY_NAME));
            assertThat(resultSet.getString(3), is(AUTH_TICKET.getSecret()));
        }
    }

    @Test
    public void shouldHavePlayer() {
        playerRepository.addPlayer(PLAYER_ID, DISPLAY_NAME, AUTH_TICKET);
        assertThat(playerRepository.hasPlayer(PLAYER_ID), is(true));
    }

    @Test
    public void shouldGetDisplayName() {
        playerRepository.addPlayer(PLAYER_ID, DISPLAY_NAME, AUTH_TICKET);
        assertThat(playerRepository.getDisplayName(PLAYER_ID), is(DISPLAY_NAME));
    }

    @Test
    public void shouldSetDisplayName() {
        playerRepository.addPlayer(PLAYER_ID, DISPLAY_NAME, AUTH_TICKET);
        final String newName = "bob";
        playerRepository.setDisplayName(PLAYER_ID, newName);
        assertThat(playerRepository.getDisplayName(PLAYER_ID), is(newName));
    }

    @Test
    public void shouldGetTicket() {
        playerRepository.addPlayer(PLAYER_ID, DISPLAY_NAME, AUTH_TICKET);
        assertThat(playerRepository.getTicket(PLAYER_ID).get(), is(AUTH_TICKET));
    }

    @Test
    public void shouldSetTicket() {
        playerRepository.addPlayer(PLAYER_ID, DISPLAY_NAME, AUTH_TICKET);
        final Ticket newTicket = new Ticket(PLAYER_ID);
        playerRepository.setTicket(newTicket);
        assertThat(playerRepository.getTicket(PLAYER_ID).get(), is(newTicket));
    }

    @After
    public void after() throws SQLException {
        connection.createStatement().execute("DROP ALL OBJECTS DELETE FILES");
        connection.close();
    }

}

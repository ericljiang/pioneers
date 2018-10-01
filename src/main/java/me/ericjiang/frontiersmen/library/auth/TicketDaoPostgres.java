package me.ericjiang.frontiersmen.library.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TicketDaoPostgres implements TicketDao {

    private final PreparedStatement writeTicket;

    private final PreparedStatement readTicket;

    public TicketDaoPostgres(Connection connection) {
        try {
            this.writeTicket = connection.prepareStatement(
                    "INSERT INTO auth_ticket VALUES(?, ?) " +
                    "ON CONFLICT (player_id) DO UPDATE SET secret = EXCLUDED.secret");

            this.readTicket = connection.prepareStatement(
                    "SELECT secret FROM auth_ticket WHERE player_id=?");
        } catch (SQLException e) {
            String message = "Failed to prepare SQL statements.";
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public Optional<Ticket> getTicket(String playerId) {
        try {
            readTicket.setString(1, playerId);
            final ResultSet resultSet = readTicket.executeQuery();
            final Optional<Ticket> ticket = Optional.ofNullable(
                    resultSet.next() ? resultSet.getString(1) : null)
                    .map(secret -> new Ticket(playerId, secret));
            resultSet.close();
            return ticket;
        } catch (SQLException e) {
            log.error("Failed to read auth ticket from PostgreSQL", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putTicket(Ticket ticket) {
        try {
            writeTicket.setString(1, ticket.getPlayerId());
            writeTicket.setString(2, ticket.getSecret());
            writeTicket.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to write auth ticket to PostgreSQL", e);
            throw new RuntimeException(e);
        }
    }

}

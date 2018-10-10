package me.ericjiang.frontiersmen.library.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.library.auth.Ticket;

@Slf4j
public class PlayerRepositoryPostgres implements PlayerRepository {

    private final PreparedStatement hasPlayer;

    private final PreparedStatement addPlayer;

    private final PreparedStatement writeDisplayName;

    private final PreparedStatement readDisplayName;

    private final PreparedStatement writeTicket;

    private final PreparedStatement readTicket;

    public PlayerRepositoryPostgres(Connection connection) {
        try {
            this.hasPlayer = connection.prepareStatement(
                    "SELECT EXISTS (SELECT 1 FROM player WHERE player_id = ?)");
            this.addPlayer = connection.prepareStatement(
                    "INSERT INTO player VALUES(?, ?, ?)");
            this.writeDisplayName = connection.prepareStatement(
                    "UPDATE player SET display_name = ? WHERE player_id = ?");
            this.readDisplayName = connection.prepareStatement(
                    "SELECT display_name FROM player WHERE player_id = ?");
            this.writeTicket = connection.prepareStatement(
                    "UPDATE player SET secret = ? WHERE player_id = ?");
            this.readTicket = connection.prepareStatement(
                    "SELECT secret FROM player WHERE player_id = ?");
        } catch (SQLException e) {
            String message = "Failed to prepare SQL statements.";
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public boolean hasPlayer(String playerId) {
        try {
            hasPlayer.setString(1, playerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (ResultSet resultSet = hasPlayer.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            } else {
                throw new SQLException("No rows returned");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPlayer(String playerId, String name, Ticket ticket) {
        try {
            addPlayer.setString(1, playerId);
            addPlayer.setString(2, name);
            addPlayer.setString(3, ticket.getSecret());
            int updates = addPlayer.executeUpdate();
            if (updates != 1) {
                throw new SQLException("Number of rows updated should be 1 but was " + updates);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDisplayName(String playerId) {
        log.info("Getting display name for {} from PostgreSQL database...", playerId);
        try {
            readDisplayName.setString(1, playerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (ResultSet resultSet = readDisplayName.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                log.error("No display name found for player {}", playerId);
                return "?";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setDisplayName(String playerId, String name) {
        try {
            writeDisplayName.setString(1, name);
            writeDisplayName.setString(2, playerId);
            int updates = writeDisplayName.executeUpdate();
            if (updates != 1) {
                throw new SQLException("Number of rows updated should be 1 but was " + updates);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Ticket> getTicket(String playerId) {
        log.info("Getting ticket for {} from PostgreSQL database...", playerId);
        try {
            readTicket.setString(1, playerId);
            try (ResultSet resultSet = readTicket.executeQuery()) {
                return Optional.ofNullable(
                        resultSet.next() ? resultSet.getString(1) : null)
                        .map(secret -> new Ticket(playerId, secret));
            }
        } catch (SQLException e) {
            log.error("Failed to read auth ticket from PostgreSQL", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTicket(Ticket ticket) {
        try {
            writeTicket.setString(1, ticket.getSecret());
            writeTicket.setString(2, ticket.getPlayerId());
            int updates = writeTicket.executeUpdate();
            if (updates != 1) {
                throw new SQLException("Number of rows updated should be 1 but was " + updates);
            }
        } catch (SQLException e) {
            log.error("Failed to write auth ticket to PostgreSQL", e);
            throw new RuntimeException(e);
        }
    }

}

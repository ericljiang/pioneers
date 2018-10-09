package me.ericjiang.frontiersmen.library.player;

import java.util.Optional;

import me.ericjiang.frontiersmen.library.auth.Ticket;

public interface PlayerRepository {

    boolean hasPlayer(String playerId);

    void addPlayer(String playerId, String name, Ticket ticket);

    String getDisplayName(String playerId);

    void setDisplayName(String playerId, String name);

    Optional<Ticket> getTicket(String playerId);

    void setTicket(Ticket ticket);

}

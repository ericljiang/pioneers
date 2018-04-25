package me.ericjiang.frontiersmen.library.auth;

import java.util.Optional;

public interface TicketDao {

    Optional<Ticket> getTicket(String playerId);

    void putTicket(String playerId, Ticket ticket);

}

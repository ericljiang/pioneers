package me.ericjiang.frontiersmen.library.auth;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

public class TicketDaoInMemory implements TicketDao {

    /**
     * Maps PlayerId strings to Tickets
     */
    private final Map<String, Ticket> tickets;

    public TicketDaoInMemory() {
        this.tickets = Maps.newConcurrentMap();
    }

    @Override
    public Optional<Ticket> getTicket(String playerId) {
        return Optional.ofNullable(tickets.get(playerId));
    }

    @Override
    public void putTicket(Ticket ticket) {
        tickets.put(ticket.getPlayerId(), ticket);
    }

}

package me.ericjiang.frontiersmen.library.auth;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {

    private final String playerId;

    private final String secret;

    public Ticket(String playerId) {
        this.playerId = playerId;
        this.secret = new BigInteger(130, new SecureRandom()).toString(32);
    }

    public static Ticket fromString(String stringTicket) {
        return new Gson().fromJson(stringTicket, Ticket.class);
    }
}

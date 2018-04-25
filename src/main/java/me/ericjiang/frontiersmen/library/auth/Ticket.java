package me.ericjiang.frontiersmen.library.auth;

import java.math.BigInteger;
import java.security.SecureRandom;

import lombok.Data;

@Data
public class Ticket {

    private final String playerId;

    private final String secret;

    public Ticket(String playerId) {
        this.playerId = playerId;
        this.secret = new BigInteger(130, new SecureRandom()).toString(32);
    }
}

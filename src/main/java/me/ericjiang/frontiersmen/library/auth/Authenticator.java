package me.ericjiang.frontiersmen.library.auth;

import java.security.GeneralSecurityException;

public interface Authenticator {

    /**
     * @param playerId
     * @param authToken
     * @return The user's display name
     */
    String verify(String playerId, String authToken) throws GeneralSecurityException;

}

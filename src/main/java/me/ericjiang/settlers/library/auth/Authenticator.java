package me.ericjiang.settlers.library.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface Authenticator {

    /**
     * @param playerId
     * @param authToken
     * @return The user's display name
     */
    String verify(String playerId, String authToken) throws GeneralSecurityException, IOException;

}

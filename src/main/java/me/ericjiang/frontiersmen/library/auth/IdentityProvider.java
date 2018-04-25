package me.ericjiang.frontiersmen.library.auth;

import java.security.GeneralSecurityException;

public interface IdentityProvider {

    /**
     * @param playerId
     * @param authToken
     * @throws GeneralSecurityException
     */
    void verify(String playerId, String authToken) throws GeneralSecurityException;

    String getName(String authToken) throws GeneralSecurityException;

}

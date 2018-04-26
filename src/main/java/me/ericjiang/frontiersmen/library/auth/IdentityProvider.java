package me.ericjiang.frontiersmen.library.auth;

import java.security.GeneralSecurityException;

public interface IdentityProvider {

    /**
     * @param playerId
     * @param idToken
     * @throws GeneralSecurityException
     */
    void verify(String playerId, String idToken) throws GeneralSecurityException;

    String getName(String idToken) throws GeneralSecurityException;

}

package me.ericjiang.frontiersmen.library.auth;

import java.security.GeneralSecurityException;

public interface IdentityProvider {

    /**
     * Ensures that an idToken is valid for the given playerId.
     *
     * @param playerId owner of the idToken
     * @param idToken idToken to verify
     * @throws GeneralSecurityException if the idToken is invalid or does not
     * belong to the given playerId
     */
    void verify(String playerId, String idToken) throws GeneralSecurityException;

    /**
     * Provides a display name for the owner of the idToken.
     */
    String getName(String idToken) throws GeneralSecurityException;

}

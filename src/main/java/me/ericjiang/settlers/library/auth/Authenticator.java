package me.ericjiang.settlers.library.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface Authenticator {

    String verify(String playerId, String authToken) throws GeneralSecurityException, IOException;

}

package me.ericjiang.frontiersmen.library.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MockAuthenticator implements Authenticator {

    @Override
    public String verify(String playerId, String authToken) throws GeneralSecurityException, IOException {
        return "Foo";
    }

}

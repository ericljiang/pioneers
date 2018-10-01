package me.ericjiang.frontiersmen.library.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.security.GeneralSecurityException;

@AllArgsConstructor
public class GoogleIdentityProvider implements IdentityProvider {

    private final GoogleIdTokenVerifier verifier;

    @Override
    public void verify(String playerId, String idTokenString) throws GeneralSecurityException {
        final GoogleIdToken idToken = parseIdToken(idTokenString);
        final String userId = idToken.getPayload().getSubject();
        if (!playerId.equals(userId)) {
            throw new GeneralSecurityException(
                    String.format("Player %s attempted to impersonate player %s", playerId, userId));
        }
    }

    @Override
    public String getName(String idTokenString) {
        try {
            final GoogleIdToken idToken = parseIdToken(idTokenString);
            return (String) idToken.getPayload().get("given_name");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private GoogleIdToken parseIdToken(String idTokenString) throws GeneralSecurityException {
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (IOException e) {
            throw new GeneralSecurityException("Invalid idTokenString", e);
        }
        if (idToken == null) {
            throw new GeneralSecurityException("Invalid idTokenString");
        }
        return idToken;
    }

}

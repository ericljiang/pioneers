package me.ericjiang.frontiersmen.library.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.security.GeneralSecurityException;

@AllArgsConstructor
public class GoogleAuthenticator implements Authenticator {

    private final GoogleIdTokenVerifier verifier;

    @Override
    public String verify(String playerId, String authToken) throws GeneralSecurityException {
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Failed to verify Google ID token.", e);
        }
        String userId = idToken.getPayload().getSubject();
        if (!playerId.equals(userId)) {
            throw new GeneralSecurityException("ID token does not match playerId.");
        }
        return (String) idToken.getPayload().get("given_name");
    }

}

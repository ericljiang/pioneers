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
        try {
            GoogleIdToken idToken = verifier.verify(authToken);
            if (idToken == null) {
                throw new GeneralSecurityException("Invalid authorization token.");
            }
            String userId = idToken.getPayload().getSubject();
            if (!playerId.equals(userId)) {
                throw new GeneralSecurityException(String.format(
                        "Authorization token does not belong to player %s.", playerId));
            }
            return (String) idToken.getPayload().get("given_name");
        } catch (IOException e) {
            throw new RuntimeException("Failed to verify Google ID token.", e);
        }
    }

}

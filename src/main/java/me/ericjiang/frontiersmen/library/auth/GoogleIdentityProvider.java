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
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new GeneralSecurityException();
            }
            String userId = idToken.getPayload().getSubject();
            if (!playerId.equals(userId)) {
                throw new GeneralSecurityException();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to verify Google ID token.", e);
        }
    }

    @Override
    public String getName(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new GeneralSecurityException();
            }
            return (String) idToken.getPayload().get("given_name");
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}

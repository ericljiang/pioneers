package me.ericjiang.settlers.library.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleAuthenticator implements Authenticator {

    private final GoogleIdTokenVerifier verifier;

    private static final String CLIENT_ID = "224119011410-5hbr37e370ieevfk9t64v9799kivttan.apps.googleusercontent.com";

    public GoogleAuthenticator() {
        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        this.verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();
    }

    @Override
    public void verify(String playerId, String authToken) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(authToken);
        if (idToken == null) {
            throw new GeneralSecurityException("Invalid ID token.");
        }
        String userId = idToken.getPayload().getSubject();
        if (userId != playerId) {
            throw new GeneralSecurityException("ID token does not match playerId");
        }
    }

}

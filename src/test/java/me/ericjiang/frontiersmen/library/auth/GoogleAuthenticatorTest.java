package me.ericjiang.frontiersmen.library.auth;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

public class GoogleAuthenticatorTest extends EasyMockSupport {

    private static final String PLAYER_ID = "1";

    private static final String AUTH_TOKEN = "aaa";

    private GoogleIdTokenVerifier tokenVerifier;

    private GoogleAuthenticator authenticator;

    @Before
    public void setup() {
        tokenVerifier = createMock(GoogleIdTokenVerifier.class);
        authenticator = new GoogleAuthenticator(tokenVerifier);
    }

    @Test(expected = GeneralSecurityException.class)
    public void test() throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = createMock(GoogleIdToken.class);
        Payload payload = new Payload();
        payload.setSubject("0");

        expect(tokenVerifier.verify(AUTH_TOKEN)).andReturn(idToken);
        expect(idToken.getPayload()).andReturn(payload);
        replayAll();

        authenticator.verify(PLAYER_ID, AUTH_TOKEN);
    }

}

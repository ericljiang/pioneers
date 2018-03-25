package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;

import java.util.Collections;

import javax.inject.Singleton;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.GoogleAuthenticator;

@Module
public class AuthenticatorModule {

    private static final String CLIENT_ID = "224119011410-5hbr37e370ieevfk9t64v9799kivttan.apps.googleusercontent.com";

    @Provides @Singleton static Authenticator provideAuthenticator(GoogleIdTokenVerifier googleIdTokenVerifier) {
        return new GoogleAuthenticator(googleIdTokenVerifier);
    }

    @Provides @Singleton static GoogleIdTokenVerifier provideGoogleIdTokenVerifier() {
        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }

}

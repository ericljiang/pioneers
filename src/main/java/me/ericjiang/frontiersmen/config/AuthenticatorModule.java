package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;

import java.util.Collections;

import javax.inject.Singleton;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.GoogleIdentityProvider;
import me.ericjiang.frontiersmen.library.auth.IdentityProvider;
import me.ericjiang.frontiersmen.library.auth.MockIdentityProvider;
import me.ericjiang.frontiersmen.library.auth.TicketDao;
import me.ericjiang.frontiersmen.library.auth.TicketDaoInMemory;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

@Module(includes = { PlayerRepositoryModule.class })
public class AuthenticatorModule {

    private static final String CLIENT_ID = "224119011410-5hbr37e370ieevfk9t64v9799kivttan.apps.googleusercontent.com";

    @Provides @Singleton static Authenticator provideAuthenticator(IdentityProvider identityProvider, TicketDao ticketDao, PlayerRepository playerRepository) {
        return new Authenticator(new MockIdentityProvider(), ticketDao, playerRepository);
    }

    @Provides @Singleton static IdentityProvider provideIdentityProvider(GoogleIdTokenVerifier googleIdTokenVerifier) {
        return new GoogleIdentityProvider(googleIdTokenVerifier);
    }

    @Provides @Singleton static GoogleIdTokenVerifier provideGoogleIdTokenVerifier() {
        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }

    @Provides @Singleton static TicketDao providTicketDao() {
        return new TicketDaoInMemory();
    }

}

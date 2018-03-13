package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.GoogleAuthenticator;

@Module
public class AuthenticatorModule {

    @Provides @Singleton static Authenticator provideAuthenticator() {
        return new GoogleAuthenticator();
    }

}

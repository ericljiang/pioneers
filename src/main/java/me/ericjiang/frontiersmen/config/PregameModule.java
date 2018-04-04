package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.library.pregame.PregameEventRouter;
import me.ericjiang.frontiersmen.websockets.WebSocketTranslator;

@Module(includes = {
    LobbyModule.class,
    AuthenticatorModule.class,
    PlayerRepositoryModule.class
})
public class PregameModule {

    @Provides @Named("pregameWebSocketHandler") static WebSocketTranslator providePregameWebSocketHandler(
        PregameEventRouter pregameEventRouter,
            PlayerRepository playerRepository) {
        return new WebSocketTranslator(pregameEventRouter, playerRepository);
    }

    @Provides @Singleton static PregameEventRouter providePregameEventRouter(
            Lobby lobby,
            Authenticator authenticator,
            PlayerRepository playerRepository) {
        return new PregameEventRouter(lobby, authenticator, playerRepository);
    }

}

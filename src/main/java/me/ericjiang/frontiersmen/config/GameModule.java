package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.game.GameEventRouter;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.websockets.WebSocketTranslator;

@Module(includes = {
    LobbyModule.class,
    AuthenticatorModule.class,
    PlayerRepositoryModule.class
})
public class GameModule {

    @Provides @Named("gameWebSocketHandler") static WebSocketTranslator provideGameWebSocketHandler(
            GameEventRouter gameEventRouter,
            PlayerRepository playerRepository) {
        return new WebSocketTranslator(gameEventRouter, playerRepository);
    }

    @Provides @Singleton static GameEventRouter provideGameEventRouter(
            Lobby lobby,
            Authenticator authenticator,
            PlayerRepository playerRepository) {
        return new GameEventRouter(lobby, authenticator, playerRepository);
    }

}

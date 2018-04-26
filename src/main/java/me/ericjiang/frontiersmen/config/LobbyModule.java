package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.lobby.LobbyEventRouter;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.websockets.WebSocketTranslator;

@Module(includes = {
    AuthenticatorModule.class,
    PlayerRepositoryModule.class,
    GameFactoryModule.class
})
public class LobbyModule {

    @Provides @Named("lobbyWebSocketHandler") static WebSocketTranslator provideLobbyWebSocketHandler(
            LobbyEventRouter lobbyEventRouter,
            PlayerRepository playerRepository) {
        return new WebSocketTranslator(lobbyEventRouter, playerRepository);
    }

    @Provides @Singleton static LobbyEventRouter provideLobbyEventRouter(
            Lobby lobby,
            Authenticator authenticator,
            PlayerRepository playerRepository) {
        return new LobbyEventRouter(lobby, authenticator);
    }

    /**
     * TODO: https://github.com/frontiersmen/frontiersmen-server/issues/66
     */
    @Provides @Singleton static Lobby provideLobby(GameFactory gameFactory, PlayerRepository playerRepository) {
        return new Lobby(gameFactory, playerRepository);
    }

}

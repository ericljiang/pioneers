package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.lobby.LobbyWebSocketHandler;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

@Module(includes = {
    AuthenticatorModule.class,
    PlayerRepositoryModule.class,
    GameFactoryModule.class
})
public class LobbyModule {

    @Provides @Singleton static LobbyWebSocketHandler provideLobbyWebSocketHandler(
            Lobby lobby,
            Authenticator authenticator,
            PlayerRepository playerRepository) {
        return new LobbyWebSocketHandler(lobby, authenticator, playerRepository);
    }

    /**
     * TODO: https://github.com/frontiersmen/frontiersmen-server/issues/66
     */
    @Provides @Singleton static Lobby provideLobby(GameFactory gameFactory, PlayerRepository playerRepository) {
        return new Lobby(gameFactory, playerRepository);
    }

}

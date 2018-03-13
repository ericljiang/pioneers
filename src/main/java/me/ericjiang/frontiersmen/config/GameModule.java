package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.game.GameWebSocketRouter;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

@Module(includes = {
    LobbyModule.class,
    AuthenticatorModule.class,
    PlayerRepositoryModule.class
})
public class GameModule {

    @Provides @Singleton static GameWebSocketRouter provideGameWebSocketRouter(
            Lobby lobby,
            Authenticator authenticator,
            PlayerRepository playerRepository) {
        return new GameWebSocketRouter(lobby, authenticator, playerRepository);
    }

}

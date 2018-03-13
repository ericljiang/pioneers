package me.ericjiang.frontiersmen.config;

import com.google.gson.Gson;
import dagger.Component;
import javax.inject.Singleton;
import me.ericjiang.frontiersmen.library.game.GameWebSocketRouter;
import me.ericjiang.frontiersmen.library.lobby.LobbyWebSocketHandler;

@Component(modules = {
    DatabaseModule.class,
    FrontiersmenModule.class,
    GameModule.class,
    LobbyModule.class
})
@Singleton
public interface Frontiersmen {

    int port();

    Gson gson();

    LobbyWebSocketHandler lobbyWebSocketHandler();

    GameWebSocketRouter gameWebSocketRouter();

}

package me.ericjiang.frontiersmen.config;

import java.sql.Connection;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import me.ericjiang.frontiersmen.library.game.GameDao;
import me.ericjiang.frontiersmen.library.game.GameDaoPostgres;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.simple.SimpleGame;
import me.ericjiang.frontiersmen.simple.SimpleGameFactory;

@Module(includes = { DatabaseModule.class })
public class GameFactoryModule {

    @Provides @Singleton static GameFactory provideGameFactory(GameDao gameDao) {
        return new SimpleGameFactory(gameDao);
    }

    @Provides @Singleton static GameDao provideGameDao(Connection connection) {
        return new GameDaoPostgres(SimpleGame.class, connection);
    }

}

package me.ericjiang.frontiersmen.config;

import java.sql.Connection;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import me.ericjiang.frontiersmen.library.game.GameDao;
import me.ericjiang.frontiersmen.library.game.GameDaoPostgres;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.tictactoe.TicTacToeGame;
import me.ericjiang.frontiersmen.tictactoe.TicTacToeGameFactory;;


@Module(includes = { DatabaseModule.class })
public class GameFactoryModule {

    @Provides @Singleton static GameFactory provideGameFactory(GameDao gameDao) {
        return new TicTacToeGameFactory(gameDao);
    }

    @Provides @Singleton static GameDao provideGameDao(Connection connection) {
        return new GameDaoPostgres(TicTacToeGame.class, connection);
    }

}

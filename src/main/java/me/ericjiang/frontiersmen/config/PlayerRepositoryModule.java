package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;

import java.sql.Connection;

import javax.inject.Singleton;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.library.player.PlayerRepositoryPostgres;

@Module(includes = { DatabaseModule.class })
public class PlayerRepositoryModule {

    @Provides @Singleton static PlayerRepository providePlayerRepository(Connection connection) {
        return new PlayerRepositoryPostgres(connection);
    }

}

package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.ericjiang.frontiersmen.library.player.InMemoryPlayerRepository;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

@Module
public class PlayerRepositoryModule {

    @Provides @Singleton static PlayerRepository providePlayerRepository() {
        return new InMemoryPlayerRepository();
    }

}

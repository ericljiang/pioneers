package me.ericjiang.frontiersmen.config;

import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import com.google.gson.Gson;

@Module
public class FrontiersmenModule {

    private static final String HEROKU_PORT_VARIABLE = "PORT";

    private static final int DEFAULT_PORT = 4567;

    /**
     * Return default port if Heroku port isn't set (i.e. on localhost)
     */
    @Provides static int providePort() {
        return Optional.ofNullable(System.getenv(HEROKU_PORT_VARIABLE))
        .map(s -> Integer.parseInt(s))
        .orElse(DEFAULT_PORT);
    }

    @Provides static Gson gson() {
        return new Gson();
    }
}

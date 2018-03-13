package me.ericjiang.frontiersmen;

import static spark.Spark.*;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import me.ericjiang.frontiersmen.config.DaggerFrontiersmen;
import me.ericjiang.frontiersmen.config.Frontiersmen;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.GoogleAuthenticator;
import me.ericjiang.frontiersmen.library.game.GameDao;
import me.ericjiang.frontiersmen.library.game.GameDaoPostgres;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.library.game.GameWebSocketRouter;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.lobby.LobbyWebSocketHandler;
import me.ericjiang.frontiersmen.library.player.InMemoryPlayerRepository;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.simple.SimpleGame;
import me.ericjiang.frontiersmen.simple.SimpleGameFactory;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        log.info("+-----------------+");
        log.info("| Starting server |");
        log.info("+-----------------+");

        Frontiersmen frontiersmen = DaggerFrontiersmen.create();

        port(frontiersmen.port());
        staticFiles.location("/public");

        webSocket("/ws/lobby", frontiersmen.lobbyWebSocketHandler());
        webSocket("/ws/game", frontiersmen.gameWebSocketRouter());
        get("/api/hello", (req, res) -> new Greeting(), frontiersmen.gson()::toJson);
        get("/ping", (req, res) -> "pong");
    }
}

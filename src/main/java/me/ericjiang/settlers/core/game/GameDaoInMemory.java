package me.ericjiang.settlers.core.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.board.BoardDao;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.game.GameFactory;
import me.ericjiang.settlers.core.player.PlayerDao;

@Slf4j
public class GameDaoInMemory implements GameDao {

    // TODO: replace with database gameDAO.getGame(id)
    private Map<String, Game> games;

    public GameDaoInMemory() {
        games = new HashMap<String, Game>();
    }

    @Override
    public void createGame(String name, String expansion, BoardDao boardDao, PlayerDao playerDao) {
        log.info(String.format("Creating game '%s' with expansion '%s'.", name, expansion));
        Game game = GameFactory.newGame(name, expansion, boardDao, playerDao);
        games.put(game.getId(), game);
    }

    @Override
    public List<Game> gamesForPlayer(String playerId) {
        return games.values().stream()
                .filter(game -> game.hasPlayer(playerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Game> openGames() {
        return games.values().stream()
                .filter(game -> game.isOpen())
                .collect(Collectors.toList());
    }

    @Override
    public List<Game> openGamesWithoutPlayer(String playerId) {
        return games.values().stream()
                .filter(game -> game.isOpen() && !game.hasPlayer(playerId))
                .collect(Collectors.toList());
    }

    @Override
    public Game getGame(String id) {
        return games.get(id);
    }
}

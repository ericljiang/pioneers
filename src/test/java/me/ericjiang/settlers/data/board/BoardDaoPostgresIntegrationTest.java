package me.ericjiang.settlers.data.board;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import me.ericjiang.settlers.core.board.Edge;
import me.ericjiang.settlers.core.board.Intersection;
import me.ericjiang.settlers.core.board.Tile;
import me.ericjiang.settlers.core.board.Tile.Resource;
import me.ericjiang.settlers.data.PostgresDaoIntegrationTestBase;
import me.ericjiang.settlers.spark.Settlers;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Sets;

public class BoardDaoPostgresIntegrationTest extends PostgresDaoIntegrationTestBase {

    private static BoardDao boardDao;

    @BeforeClass
    public static void beforeClass() throws SQLException {
        Connection connection = Settlers.getDatabaseConnection();
        boardDao = new BoardDaoPostgres(connection);
    }

    @Test
    public void shouldGetTileWePut() {
        Tile.Coordinates coordinates = new Tile.Coordinates(0, 0);
        Tile tile = new Tile(coordinates, Resource.NONE, 0);
        boardDao.putTile("0", coordinates, tile);
        assertEquals(tile, boardDao.getTile("0", coordinates));
        assertEquals(1, boardDao.getTiles("0").size());
    }

    @Test
    public void shouldGetEdgeWePut() {
        Edge.Coordinates coordinates = new Edge.Coordinates(0, 0, Edge.Coordinates.Direction.E);
        Edge edge = new Edge(coordinates, "foo");
        boardDao.putEdge("0", coordinates, edge);
        assertEquals(edge, boardDao.getEdge("0", coordinates));
        assertEquals(1, boardDao.getEdges("0").size());
    }

    @Test
    public void shouldGetIntersectionWePut() {
        Intersection.Coordinates coordinates = new Intersection.Coordinates(0, 0, Intersection.Coordinates.Direction.N);
        Intersection intersection = new Intersection(coordinates, "foo", false);
        boardDao.putIntersection("0", coordinates, intersection);
        assertEquals(intersection, boardDao.getIntersection("0", coordinates));
        assertEquals(1, boardDao.getIntersections("0").size());
    }

    @Override
    public Collection<String> relevantTables() {
        return Sets.newHashSet("tile", "road", "settlement");
    }
}

package me.ericjiang.settlers.data.board;

import static spark.Spark.halt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.board.Edge;
import me.ericjiang.settlers.core.board.Intersection;
import me.ericjiang.settlers.core.board.Tile;
import me.ericjiang.settlers.core.board.Tile.Resource;
import me.ericjiang.settlers.data.PostgresDao;

@Slf4j
public class BoardDaoPostgres extends PostgresDao implements BoardDao {

    public BoardDaoPostgres(Connection connection) {
        super(connection);
    }

    public Tile getTile(String gameId, Tile.Coordinates coordinates) {
        String sql = "SELECT resource, number_token FROM tile WHERE game_id = ? AND col = ? AND row = ?";
        Tile tile = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            preparedStatement.setInt(2, coordinates.getColumn());
            preparedStatement.setInt(3, coordinates.getRow());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String resource = resultSet.getString("resource");
                int numberToken = resultSet.getInt("number_token");
                tile = new Tile(coordinates, Resource.valueOf(resource.toUpperCase()), numberToken);
            }
        } catch (SQLException e) {
            log.error("Error getting tile: " + sql, e);
            halt(500);
        }
        return tile;
    }

    public Collection<Tile> getTiles(String gameId) {
        String sql = "SELECT col, row, resource, number_token FROM tile WHERE game_id = ?";
        Collection<Tile> tiles = new ArrayList<Tile>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int column = resultSet.getInt("col");
                int row = resultSet.getInt("row");
                Resource resource = Resource.valueOf(resultSet.getString("resource").toUpperCase());
                int numberToken = resultSet.getInt("number_token");
                tiles.add(new Tile(new Tile.Coordinates(column, row), resource, numberToken));
            }
        } catch (SQLException e) {
            log.error("Error getting tiles: " + sql, e);
            halt(500);
        }
        return tiles;
    }

    public void putTile(String gameId, Tile.Coordinates coordinates, Tile tile) {
        String sql = "INSERT INTO tile VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            preparedStatement.setInt(2, coordinates.getColumn());
            preparedStatement.setInt(3, coordinates.getRow());
            preparedStatement.setString(4, tile.getResource().toString());
            preparedStatement.setInt(5, tile.getNumberToken());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error putting tile: " + sql, e);
            halt(500);
        }
    }

    public Edge getEdge(String gameId, Edge.Coordinates coordinates) {
        String sql = "SELECT owner FROM road WHERE game_id = ? AND col = ? AND row = ? AND dir = ?";
        Edge edge = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            preparedStatement.setInt(2, coordinates.getColumn());
            preparedStatement.setInt(3, coordinates.getRow());
            preparedStatement.setString(4, coordinates.getDirection().toString().toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String owner = resultSet.getString("owner");
                edge = new Edge(coordinates, owner);
            }
        } catch (SQLException e) {
            log.error("Error getting edge: " + sql, e);
            halt(500);
        }
        return edge;
    }

    public Collection<Edge> getEdges(String gameId) {
        String sql = "SELECT col, row, dir, owner FROM road WHERE game_id = ?";
        Collection<Edge> edges = new ArrayList<Edge>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int column = resultSet.getInt("col");
                int row = resultSet.getInt("row");
                Edge.Coordinates.Direction direction =
                        Edge.Coordinates.Direction.valueOf(resultSet.getString("dir").toUpperCase());
                String owner = resultSet.getString("owner");
                edges.add(new Edge(new Edge.Coordinates(column, row, direction), owner));
            }
        } catch (SQLException e) {
            log.error("Error getting edges: " + sql, e);
            halt(500);
        }
        return edges;
    }

    public void putEdge(String gameId, Edge.Coordinates coordinates, Edge edge) {
        String sql = "INSERT INTO road VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            preparedStatement.setInt(2, coordinates.getColumn());
            preparedStatement.setInt(3, coordinates.getRow());
            preparedStatement.setString(4, coordinates.getDirection().toString().toLowerCase());
            preparedStatement.setString(5, edge.getOwner());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error putting tile: " + sql, e);
            halt(500);
        }
    }

    public Intersection getIntersection(String gameId, Intersection.Coordinates coordinates) {
        String sql = "SELECT owner, is_upgraded FROM settlement WHERE game_id = ? AND col = ? AND row = ? AND dir = ?";
        Intersection intersection = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            preparedStatement.setInt(2, coordinates.getColumn());
            preparedStatement.setInt(3, coordinates.getRow());
            preparedStatement.setString(4, coordinates.getDirection().toString().toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String owner = resultSet.getString("owner");
                boolean isUpgraded = resultSet.getBoolean("is_upgraded");
                intersection = new Intersection(coordinates, owner, isUpgraded);
            }
        } catch (SQLException e) {
            log.error("Error getting settlement: " + sql, e);
            halt(500);
        }
        return intersection;
    }

    public Collection<Intersection> getIntersections(String gameId) {
        String sql = "SELECT col, row, dir, owner, is_upgraded FROM settlement WHERE game_id = ?";
        Collection<Intersection> intersections = new ArrayList<Intersection>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int column = resultSet.getInt("col");
                int row = resultSet.getInt("row");
                Intersection.Coordinates.Direction direction =
                        Intersection.Coordinates.Direction.valueOf(resultSet.getString("dir").toUpperCase());
                String owner = resultSet.getString("owner");
                boolean isUpgraded = resultSet.getBoolean("is_upgraded");
                Intersection.Coordinates coordinates = new Intersection.Coordinates(column, row, direction);
                intersections.add(new Intersection(coordinates, owner, isUpgraded));
            }
        } catch (SQLException e) {
            log.error("Error getting settlements: " + sql, e);
            halt(500);
        }
        return intersections;
    }

    public void putIntersection(String gameId, Intersection.Coordinates coordinates, Intersection intersection) {
        String sql = "INSERT INTO settlement VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            preparedStatement.setInt(2, coordinates.getColumn());
            preparedStatement.setInt(3, coordinates.getRow());
            preparedStatement.setString(4, coordinates.getDirection().toString().toLowerCase());
            preparedStatement.setString(5, intersection.getOwner());
            preparedStatement.setBoolean(6, intersection.isUpgraded());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error putting intersection: " + sql, e);
            halt(500);
        }
    }
}

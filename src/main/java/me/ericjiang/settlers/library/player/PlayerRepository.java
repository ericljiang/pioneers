package me.ericjiang.settlers.library.player;

public interface PlayerRepository {

    String getDisplayName(String playerId);

    void setDisplayName(String playerId, String name);

    boolean contains(String playerId);

}

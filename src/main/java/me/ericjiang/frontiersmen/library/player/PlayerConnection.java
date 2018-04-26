package me.ericjiang.frontiersmen.library.player;

import me.ericjiang.frontiersmen.library.Event;

public interface PlayerConnection {

    static final String PLAYER_ID_PARAMETER = "playerId";

    void transmit(Event event);

    String getParameter(String parameter);

    default String getPlayerId() {
        return getParameter(PLAYER_ID_PARAMETER);
    }
}

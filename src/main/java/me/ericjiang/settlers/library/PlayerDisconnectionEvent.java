package me.ericjiang.settlers.library;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerDisconnectionEvent extends Event {

    private final String playerId;

}

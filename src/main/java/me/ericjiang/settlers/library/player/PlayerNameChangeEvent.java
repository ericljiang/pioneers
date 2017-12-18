package me.ericjiang.settlers.library.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.settlers.library.PlayerEvent;

@Getter
@AllArgsConstructor
public class PlayerNameChangeEvent extends PlayerEvent {

    private final String displayName;

    public PlayerNameChangeEvent(String playerId, String displayName) {
        setPlayerId(playerId);
        this.displayName = displayName;
    }

}

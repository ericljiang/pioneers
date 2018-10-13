package me.ericjiang.frontiersmen.library.player;

import com.google.common.base.Preconditions;

import lombok.Getter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

@Getter
public class PlayerNameChangeEvent extends PlayerEvent {

    public static final int MAX_NAME_LENGTH = 32;

    private final String displayName;

    public PlayerNameChangeEvent(String playerId, String displayName) {
        Preconditions.checkNotNull(displayName);
        final String trimmedName = displayName.trim();
        Preconditions.checkArgument(!trimmedName.isEmpty());
        Preconditions.checkArgument(trimmedName.length() <= MAX_NAME_LENGTH);
        setPlayerId(playerId);
        this.displayName = trimmedName;
    }

}

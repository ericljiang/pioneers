package me.ericjiang.frontiersmen.library.lobby;

import java.util.Map;

import com.google.common.base.Preconditions;

import lombok.Getter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

@Getter
public class GameCreationEvent extends PlayerEvent {

    public static final int MAX_NAME_LENGTH = 32;

    private final String name;

    private final Map<String, Object> attributes;

    public GameCreationEvent(String name, Map<String, Object> attributes) {
        Preconditions.checkNotNull(name);
        final String trimmedName = name.trim();
        Preconditions.checkArgument(!trimmedName.isEmpty());
        Preconditions.checkArgument(trimmedName.length() <= MAX_NAME_LENGTH);
        this.name = trimmedName;
        this.attributes = attributes;
    }

}

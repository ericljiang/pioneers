package me.ericjiang.frontiersmen.library.lobby;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

@Getter
@AllArgsConstructor
public class GameCreationEvent extends PlayerEvent {

    private final Map<String, Object> attributes;

}

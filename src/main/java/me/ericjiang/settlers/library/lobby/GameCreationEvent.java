package me.ericjiang.settlers.library.lobby;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.settlers.library.Event;

@Getter
@AllArgsConstructor
public class GameCreationEvent extends Event {

    private final Map<String, Object> attributes;

}

package me.ericjiang.settlers.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class PlayerConnectionEvent extends Event {

    private final String playerId;

}

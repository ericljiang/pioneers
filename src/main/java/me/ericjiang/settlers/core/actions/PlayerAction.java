package me.ericjiang.settlers.core.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PlayerAction extends Action {

    private String gameId;

    private String playerId;

}
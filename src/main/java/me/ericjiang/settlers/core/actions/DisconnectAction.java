package me.ericjiang.settlers.core.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DisconnectAction extends Action {

    private String playerId;

    private String playerName;

}

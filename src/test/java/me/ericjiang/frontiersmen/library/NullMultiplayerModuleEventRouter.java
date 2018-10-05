package me.ericjiang.frontiersmen.library;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import me.ericjiang.frontiersmen.library.player.PlayerConnection;

public class NullMultiplayerModuleEventRouter extends MultiplayerModuleEventRouter {

    public NullMultiplayerModuleEventRouter() {
        super(null);
    }

    @Override
    public List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList();
    }

    @Override
    protected Optional<? extends MultiplayerModule> getModule(PlayerConnection connection) {
        throw new UnsupportedOperationException();
    }

}

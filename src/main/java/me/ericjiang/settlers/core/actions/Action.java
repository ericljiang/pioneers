package me.ericjiang.settlers.core.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.ericjiang.settlers.util.RuntimeTypeAdapterFactory;
import me.ericjiang.settlers.util.ShortUUID;

public abstract class Action {

    /**
     * Base-64 encoded UUID without padding
     */
    @Getter
    private final String id;

    private static final Gson GSON;

    static {
        RuntimeTypeAdapterFactory<Action> actionAdapter = RuntimeTypeAdapterFactory.of(Action.class)
                .registerSubtype(ConnectAction.class)
                .registerSubtype(DisconnectAction.class)
                .registerSubtype(JoinAction.class)
                .registerSubtype(LeaveAction.class);
        GSON = new GsonBuilder()
                .registerTypeAdapterFactory(actionAdapter)
                .create();
    }

    public Action() {
        this.id = ShortUUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return GSON.toJson(this, Action.class);
    }

    public static Action valueOf(String string) {
        return GSON.fromJson(string, Action.class);
    }
}

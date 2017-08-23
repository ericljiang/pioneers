package me.ericjiang.settlers.core.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.ericjiang.settlers.util.RuntimeTypeAdapterFactory;
import me.ericjiang.settlers.util.ShortUUID;

@EqualsAndHashCode
public abstract class Action {

    /**
     * Base-64 encoded UUID without padding
     */
    @Getter
    private final String id;

    private static final Gson GSON;

    static {
        RuntimeTypeAdapterFactory<Action> actionAdapter = RuntimeTypeAdapterFactory.of(Action.class)
                .registerSubtype(GameUpdate.class)
                .registerSubtype(ConnectAction.class)
                .registerSubtype(DisconnectAction.class)
                .registerSubtype(JoinAction.class)
                .registerSubtype(LeaveAction.class)
                .registerSubtype(StartAction.class);
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

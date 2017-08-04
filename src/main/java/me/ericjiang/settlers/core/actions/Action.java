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
    private String id;

    private static final Gson GSON;

    static {
        RuntimeTypeAdapterFactory<Action> actionAdapter = RuntimeTypeAdapterFactory.of(Action.class)
                .registerSubtype(SimpleAction.class);
        GSON = new GsonBuilder()
                .registerTypeAdapterFactory(actionAdapter)
                .create();
    }

    public Action() {
        id = ShortUUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return GSON.toJson(this, Action.class);
    }

    public static Action valueOf(String string) {
        return GSON.fromJson(string, Action.class);
    }
}
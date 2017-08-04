package me.ericjiang.settlers.core.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.UUID;
import lombok.Getter;
import me.ericjiang.settlers.util.RuntimeTypeAdapterFactory;

public abstract class Action {

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
        id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return GSON.toJson(this, Action.class);
    }

    public static Action valueOf(String string) {
        return GSON.fromJson(string, Action.class);
    }
}
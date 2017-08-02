package me.ericjiang.settlers.core.actions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import me.ericjiang.settlers.util.RuntimeTypeAdapterFactory;

public abstract class Action {

    @Getter
    @Setter
    private String id;

    private static Gson gson;

    static {
        RuntimeTypeAdapterFactory<Action> actionAdapter = RuntimeTypeAdapterFactory.of(Action.class)
                .registerSubtype(SimpleAction.class);
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(actionAdapter)
                .create();
    }

    public Action() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return gson.toJson(this, Action.class);
    }

    public static Action valueOf(String string) {
        return gson.fromJson(string, Action.class);
    }
}
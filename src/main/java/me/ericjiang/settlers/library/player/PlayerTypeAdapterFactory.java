package me.ericjiang.settlers.library.player;

import com.google.gson.JsonElement;
import me.ericjiang.settlers.library.utility.CustomizedTypeAdapterFactory;

public class PlayerTypeAdapterFactory extends CustomizedTypeAdapterFactory<Player> {

    private final PlayerRepository playerRepository;

    public PlayerTypeAdapterFactory(PlayerRepository playerRepository) {
        super(Player.class);
        this.playerRepository = playerRepository;
    }

    @Override
    protected void beforeWrite(Player source, JsonElement toSerialize) {
        String id = toSerialize.getAsJsonObject().get("id").getAsString();
        String name = playerRepository.getDisplayName(id);
        toSerialize.getAsJsonObject().addProperty("name", name);
    }

}

package me.ericjiang.frontiersmen.serialization;

import com.google.gson.JsonElement;

import me.ericjiang.frontiersmen.library.player.Player;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

public class PlayerTypeAdapterFactory extends CustomizedTypeAdapterFactory<Player> {

    private final PlayerRepository playerRepository;

    public PlayerTypeAdapterFactory(PlayerRepository playerRepository) {
        super(Player.class);
        this.playerRepository = playerRepository;
    }

    @Override
    protected void beforeWrite(Player source, JsonElement toSerialize) {
        if (toSerialize.isJsonObject()) {
            String id = toSerialize.getAsJsonObject().get("id").getAsString();
            String name = playerRepository.getDisplayName(id);
            toSerialize.getAsJsonObject().addProperty("name", name);
        }
    }

}

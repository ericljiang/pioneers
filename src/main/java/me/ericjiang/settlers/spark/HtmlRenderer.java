package me.ericjiang.settlers.spark;

import static j2html.TagCreator.*;

import me.ericjiang.settlers.core.Game;
import me.ericjiang.settlers.core.Lobby;
import me.ericjiang.settlers.spark.util.Attributes;

import spark.Request;

public class HtmlRenderer {

    public String renderLobby(Request request, Lobby lobby) {
        return document(
            html(
                body(
                    div(
                        h1("Your games"),
                        each(lobby.gamesForPlayer(request.session().attribute(Attributes.USER_ID)), game ->
                            p(game.getName())
                        )
                    ),
                    div(
                        h1("Open games"),
                        each(lobby.openGames(), game ->
                            p(game.getName())
                        )
                    )
                )
            )
        );
    }

    public String renderGame(Game game) {
        return "";
    }
}

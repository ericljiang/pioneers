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
                head(
                    meta().withName("google-signin-client_id").withContent("224119011410-5hbr37e370ieevfk9t64v9799kivttan.apps.googleusercontent.com")
                ),
                body(
                    a("Sign out").withHref("/sign-out").attr("onClick", "signOut();"),
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
                    ),
                    script().withSrc("/googleSignOut.js"),
                    script().withSrc("https://apis.google.com/js/platform.js?onload=onLoad").attr("async").attr("defer")
                )
            )
        );
    }

    public String renderGame(Game game) {
        return "";
    }
}

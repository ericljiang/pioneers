package me.ericjiang.settlers.spark.auth;

import me.ericjiang.settlers.spark.util.Attributes;

import spark.Request;
import spark.Response;

public abstract class Authenticator {

    public abstract Object renderSignInPage(Request request, Response response);

    public abstract Object signIn(Request request, Response response);

    public abstract Object signOut(Request request, Response response);

    public boolean sessionIsAuthenticated(Request request) {
        String userId = (String) request.session().attribute(Attributes.USER_ID);
        return userId != null;
    }
}

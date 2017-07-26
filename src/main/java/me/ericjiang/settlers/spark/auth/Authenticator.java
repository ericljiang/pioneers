package me.ericjiang.settlers.spark.auth;

import spark.Request;
import spark.Response;

public abstract class Authenticator {

    public static final String USER_ID = "userId";

    public static final String SIGNED_IN = "signedIn";

    public abstract Object renderSignInPage(Request request, Response response);

    public abstract Object signIn(Request request, Response response);

    public abstract Object signOut(Request request, Response response);

    public boolean sessionIsAuthenticated(Request request) {
        String userId = (String) request.session().attribute(USER_ID);
        return userId != null;
    }
}

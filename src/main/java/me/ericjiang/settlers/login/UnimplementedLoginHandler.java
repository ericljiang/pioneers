package me.ericjiang.settlers.login;

import spark.Request;
import spark.Response;

public class UnimplementedLoginHandler implements LoginHandler {

    @Override
    public String renderLoginPage() {
        return "Not yet implemented.";
    }

    public Object authenticate(Request request, Response response) {
        return "Not yet implemented.";
    }

    public Object logout(Request request, Response response) {
        return "Not yet implemented.";
    }
}
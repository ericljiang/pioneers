package me.ericjiang.settlers.login;

import spark.Request;
import spark.Response;

public interface LoginHandler {

    public Object renderLoginPage();

    public Object authenticate(Request request, Response response);

    public Object logout(Request request, Response response);
}
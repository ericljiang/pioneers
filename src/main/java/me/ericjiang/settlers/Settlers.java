package me.ericjiang.settlers;

import static spark.Spark.*;

import com.google.gson.Gson;

public class Settlers {

    public Settlers(Gson gson) {
        staticFiles.location("/public");
        get("/api/hello", (req, res) -> new Greeting(), gson::toJson);
    }

    public static void main(String[] args) {
        new Settlers(new Gson());
    }
}

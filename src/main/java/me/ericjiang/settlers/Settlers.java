package me.ericjiang.settlers;

import static spark.Spark.*;

public class Settlers {

    public Settlers() {
        staticFiles.location("/public");
        init();
    }

    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println(new Settlers().getGreeting());
    }
}

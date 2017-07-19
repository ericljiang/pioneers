package me.ericjiang.pioneers;

import static spark.Spark.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Pioneers {
    
    public static void main(String[] args) {
        log.info("Hello world.");
        port(8080);
        get(Routes.LOGIN, LoginController.loginPage);
    }
}
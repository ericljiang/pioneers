package me.ericjiang.settlers.auth;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.utils.IOUtils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.Collections;

@Slf4j
public class GoogleAuthenticator extends Authenticator {

    @Override
    @SneakyThrows
    public Object renderLoginPage(Request request, Response response) {
        return IOUtils.toString(Spark.class.getResourceAsStream("/public/googleLogin.html"));
    }

    @Override
    @SneakyThrows
    public Object login(Request request, Response response) {
        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList("224119011410-5hbr37e370ieevfk9t64v9799kivttan.apps.googleusercontent.com"))
                .build();
        String idTokenString = request.body();
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            log.info("Verified user ID token.");
            Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            log.info("User ID: " + userId);

            request.session().attribute("userId", userId);
            return userId;
        } else {
            log.warn("Invalid ID token.");
            return "null";
        }
    }

    @Override
    public Object logout(Request request, Response response) {
        return "Not yet implemented.";
    }
}
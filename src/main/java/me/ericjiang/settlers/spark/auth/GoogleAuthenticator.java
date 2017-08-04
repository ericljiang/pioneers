package me.ericjiang.settlers.spark.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.io.BaseEncoding;

import java.math.BigInteger;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import lombok.SneakyThrows;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.Spark;
import spark.utils.IOUtils;

@Slf4j
public class GoogleAuthenticator extends Authenticator {

    private static final String ID_TOKEN = "idToken";

    private static final String CLIENT_ID = "224119011410-5hbr37e370ieevfk9t64v9799kivttan.apps.googleusercontent.com";

    private static final String SIGN_IN_PAGE = "/public/googleSignIn.html";

    @Override
    @SneakyThrows
    public Object renderSignInPage(Request request, Response response) {
        return IOUtils.toString(Spark.class.getResourceAsStream(SIGN_IN_PAGE));
    }

    @Override
    @SneakyThrows
    public Object signIn(Request request, Response response) {
        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        String idTokenString = request.queryParams(ID_TOKEN);
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            log.info("Verified user ID token.");
            Payload payload = idToken.getPayload();
            String userId = encodeUserId(payload.getSubject());
            log.info("User " + userId + " signed in.");

            request.session().attribute(SIGNED_IN, true);
            request.session().attribute(USER_ID, userId);
            return userId;
        } else {
            log.warn("Invalid ID token.");
            return "null";
        }
    }

    @Override
    public Object signOut(Request request, Response response) {
        Session session = request.session();
        log.info("Signing out user " + session.attribute(USER_ID) + ".");
        session.attribute(SIGNED_IN, false);
        session.removeAttribute(USER_ID);
        response.redirect("/");
        return "200 OK";
    }

    private String encodeUserId(String userId) {
        byte[] bytes = new BigInteger(userId).toByteArray();
        return BaseEncoding.base64Url().omitPadding().encode(bytes);
    }
}

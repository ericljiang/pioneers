package me.ericjiang.frontiersmen.library.auth;

public class MockIdentityProvider implements IdentityProvider {

    @Override
    public void verify(String playerId, String idToken) {
    }

    public String getName(String idToken) {
        return "Foo";
    }

}

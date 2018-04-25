package me.ericjiang.frontiersmen.library.auth;

public class MockIdentityProvider implements IdentityProvider {

    @Override
    public void verify(String playerId, String authToken) {
    }

    public String getName(String authToken) {
        return "Foo";
    }

}

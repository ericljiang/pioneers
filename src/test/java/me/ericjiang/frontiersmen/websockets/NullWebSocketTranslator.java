package me.ericjiang.frontiersmen.websockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

public class NullWebSocketTranslator extends WebSocketTranslator implements WebSocketListener {

    public NullWebSocketTranslator() {
        super(null, null);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
    }

    @Override
    public void onWebSocketConnect(Session session) {
    }

    @Override
    public void onWebSocketError(Throwable cause) {
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
    }

    @Override
    public void onWebSocketText(String message) {
    }

}

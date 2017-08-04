// Establish the WebSocket connection and set up event handlers
var protocol;
if (location.protocol == "https:") {
    protocol = "wss:"
} else {
    protocol = "ws:"
}
var webSocketAddress = protocol + "//" + location.host + "/play" + location.search;
var webSocket = new WebSocket(webSocketAddress);
var heartbeatTimer = 0;
webSocket.onopen = onConnect;
webSocket.onmessage = onMessage;
webSocket.onclose = onClose;

function onConnect() {
    console.log("Connected to websocket at " + webSocketAddress);
    heartbeatTimer = setInterval(heartbeat, 20000);
    var action = new SimpleAction();
    var message = JSON.stringify(action);
    console.log("Sending message: " + message);
    webSocket.send(message);
}

function onMessage(message) {
    message = message.data;
    if (message == "") {
        console.debug("Received heartbeat from server.");
        return;
    }
    console.debug("Server says: " + message);
    var action = JSON.parse(message);
    console.log(String.format("Player {0} sent {1} {2}",
        action.playerId, action.type, action.id));
}

function onClose() {
    alert("WebSocket connection closed")
    clearInterval(heartbeatTimer);
}

function heartbeat() {
    if (webSocket.readyState == webSocket.OPEN) {
        webSocket.send("");
        console.debug("Sent heartbeat to server.");
    }
}
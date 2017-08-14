// Establish the WebSocket connection and set up event handlers
var protocol;
if (location.protocol == "https:") {
    protocol = "wss:"
} else {
    protocol = "ws:"
}
var webSocketAddress = protocol + "//" + location.host + "/game" + location.search;
var webSocket = new WebSocket(webSocketAddress);
var heartbeatTimer = 0;

webSocket.onopen = onConnect;
webSocket.onmessage = onMessage;
webSocket.onclose = onClose;

function onConnect() {
    console.log("Connected to websocket at " + webSocketAddress);
    heartbeatTimer = setInterval(heartbeat, 20000);
}

function onMessage(message) {
    message = message.data;
    if (message == "") {
        console.debug("Received heartbeat from server.");
        return;
    }
    console.debug("Server says: " + message);
    var action = JSON.parse(message);
    handle(action);
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

function handle(action) {
    var parser = "handle" + action.type;
    return this[parser](action);
}

function handleConnectAction(action) {
    var playerId = action.playerId;
    var playerName = action.playerName;
    console.log(playerName + " connected.");
    // update player list and player count
    $(document).ready(function() {
        $("#playerList").append("<li id=" + playerId + ">" + playerName + "</li>");
        var playerCount = $("#playerCount").text();
        var connectedPlayers = parseInt(playerCount.split("/")[0]) + 1;
        var maxPlayers = playerCount.split("/")[1];
        $("#playerCount").text(connectedPlayers + "/" + maxPlayers);
    });
}

function handleDisconnectAction(action) {
    var playerId = action.playerId;
    var playerName = action.playerName;
    console.log(playerName + " disconnected.");
    // update player list and player count
    $(document).ready(function() {
        $("#" + playerId).remove();
        var playerCount = $("#playerCount").text();
        var connectedPlayers = parseInt(playerCount.split("/")[0]) - 1;
        var maxPlayers = playerCount.split("/")[1];
        $("#playerCount").text(connectedPlayers + "/" + maxPlayers);
    });
}

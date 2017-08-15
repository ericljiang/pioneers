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

var players = new Vue({
    el: '#players',
    data: {
        count: 0,
        red: null,
        blue: null,
        white: null,
        orange: null,
        green: null,
        brown: null
    }
})

function join(color) {
    var action = new JoinAction(color);
    webSocket.send(JSON.stringify(action));
}

function leave(color) {
    var action = new LeaveAction(color);
    webSocket.send(JSON.stringify(action));
}

function handle(action) {
    var parser = "handle" + action.type;
    return this[parser](action);
}

function handleConnectAction(action) {
    // var playerId = action.playerId;
    // var playerName = action.playerName;
    // players.players.push({ name: playerName });
    // console.log(playerName + " connected.");
}

function handleDisconnectAction(action) {
    // var playerId = action.playerId;
    // var playerName = action.playerName;
    // var index = players.players.findIndex(i => i.name === playerName);
    // players.players.splice(index, 1);
    // console.log(playerName + " disconnected.");
}

function handleJoinAction(action) {
    var playerName = action.playerName;
    var color = action.color;
    players[color] = playerName;
    players.count += 1;
    console.log(playerName + " joined " + color);
}

function handleLeaveAction(action) {
    var playerName = action.playerName;
    var color = action.color;
    players[color] = null;
    players.count -= 1;
    console.log(playerName + " left " + color);
}

// Establish the WebSocket connection and set up event handlers
var protocol;
if (window.location.protocol == "https:") {
    protocol = "wss:"
} else {
    protocol = "ws:"
}
var webSocketAddress = protocol + "//" + window.location.host + "/game" + window.location.search;
var webSocket = new WebSocket(webSocketAddress);
var heartbeatTimer = 0;
var urlParams = new URLSearchParams(window.location.search);
var myPlayerId = urlParams.get("u");

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
        myColor: null,
        count: 0,
        minPlayers: null,
        maxPlayers: null,
        slots: {
            "red": null,
            "blue": null,
            "white": null,
            "orange": null,
            "green": null,
            "brown": null
        }
    },
    computed: {
        canJoin: function() {
            return this.myColor == null && this.count < this.maxPlayers;
        }
    },
    methods: {
        join: function(color) {
            var action = new JoinAction(color);
            webSocket.send(JSON.stringify(action));
        },
        leave: function(color) {
            var action = new LeaveAction(color);
            webSocket.send(JSON.stringify(action));
        },
        empty: function(color) {
            return this.slots[color] == null;
        }
    }
});

var board = new Vue({
    el: '#board',
    data: {
        tiles: [],
        edges: [],
        intersections: []
    },
    methods: {
        points: function(tile) {
            var layout = Layout(layout_pointy, Point(1, 1), Point(0, 0));
            var h = Hex(tile.coordinates.row, tile.coordinates.column);
            var corners = polygon_corners(layout, h);
            return corners.map(function(p) { return p.x + "," + p.y; }).join(" ");
        }
    }
});

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

function handleGameUpdate(update) {
    players.minPlayers = update.minPlayers;
    players.maxPlayers = update.maxPlayers;
    board.tiles = update.tiles;
}

function handleJoinAction(action) {
    var playerId = action.playerId;
    var playerName = action.playerName;
    var color = action.color;
    players.slots[color] = playerName;
    players.count += 1;
    if (playerId == myPlayerId) {
        players.myColor = color;
    }
    console.log(playerName + " joined " + color);
}

function handleLeaveAction(action) {
    var playerId = action.playerId;
    var playerName = action.playerName;
    var color = action.color;
    players.slots[color] = null;
    players.count -= 1;
    if (playerId == myPlayerId) {
        players.myColor = null;
    }
    console.log(playerName + " left " + color);
}

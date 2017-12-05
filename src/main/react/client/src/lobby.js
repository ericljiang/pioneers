export default function Lobby(playerId, authToken) {
  this.ws = new WebSocket('ws://localhost:4567/ws/lobby' +
    '?playerId=' + playerId +
    '&authToken=' + authToken);

  this.ws.onopen = () => {
    console.log('Opened WebSocket connection to Lobby');
  };

  this.ws.onclose = (closeEvent) => {
    console.log('WebSocket connection to Lobby was closed. (Reason: %s)', closeEvent.reason);
  }

  this.onMessage = function(callback) {
    this.ws.onmessage = m => {
      var data = JSON.parse(m.data);
      console.log(data);
      callback(data);
    };
  };

  this.createGame = function(owner, name) {
    console.log(owner);
    console.log("Creating game...");
    this.ws.send(JSON.stringify({
      eventType: "GameCreationEvent",
      attributes: {
        name: name
      }
    }));
  };

  this.disconnect = function() {
    this.ws.close(1000, "User disconnect");
  }
}

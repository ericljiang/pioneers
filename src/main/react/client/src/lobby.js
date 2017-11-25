export default function Lobby() {
  this.ws = new WebSocket("ws://localhost:4567/ws/lobby?playerId=1");
  this.ws.onopen = () => {
      console.log('Opened WebSocket connection to Lobby');
  };
  this.ws.onclose = (closeEvent) => {
      console.log('WebSocket connection to Lobby was closed. (Reason: %s)', closeEvent.reason);
  }
  this.onMessage = function (callback) {
    this.ws.onmessage = m => {
      var data = JSON.parse(m.data);
      console.log(data);
      callback(data);
    };
  };
  this.createGame = function (name) {
    console.log("Creating game...");
    this.ws.send(JSON.stringify({
      type: "GameCreationEvent",
      attributes: {
        name: name
      }
    }));
  };
}

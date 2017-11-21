var ws = new WebSocket("ws://localhost:4567/ws/lobby?playerId=1");

export const lobby = {
  onMessage: function (callback) {
    ws.onmessage = m => {
      var data = JSON.parse(m.data);
      console.log(data);
      callback(data);
    };
  },
  createGame: function (name) {
    console.log("Creating game...");
    ws.send(JSON.stringify({
      type: "GameCreationEvent",
      attributes: {
        name: name
      }
    }));
  }
}

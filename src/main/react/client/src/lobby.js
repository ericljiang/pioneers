import Connection from './connection.js';

export default class LobbyConnection extends Connection {
  constructor(playerId, authToken) {
    var url = 'ws://localhost:4567/ws/lobby' +
      '?playerId=' + playerId +
      '&authToken=' + authToken;
    var name = "Lobby";
    super(url, name);
    this.createGame = this.createGame.bind(this);
  }

  createGame(owner, name) {
    console.log("Creating game...");
    this.ws.send(JSON.stringify({
      eventType: "GameCreationEvent",
      attributes: {
        name: name
      }
    }));
  }
}

import WebSocketConnection from '../util/webSocketConnection.js';

export default class LobbyConnection extends WebSocketConnection {
  constructor(playerId, authToken) {
    var name = "Lobby";
    var path = "lobby";
    super(playerId, authToken, name, path);
    this.createGame = this.createGame.bind(this);
  }

  createGame(owner, name) {
    this.send({
      eventType: "GameCreationEvent",
      attributes: {
        name: name
      }
    });
  }
}

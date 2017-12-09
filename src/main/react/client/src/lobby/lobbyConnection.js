import Connection from '../util/connection.js';

export default class LobbyConnection extends Connection {
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

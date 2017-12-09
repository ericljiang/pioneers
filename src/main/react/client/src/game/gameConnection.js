import Connection from '../util/connection.js';

export default class GameConnection extends Connection {
  constructor(gameId, playerId, authToken) {
    var name = `Game ${gameId}`;
    var path = "game";
    var params = { gameId: gameId };
    super(playerId, authToken, name, path, params);
    this.startGame = this.startGame.bind(this);
  }

  startGame() {
    this.send({
      eventType: "StartGameEvent"
    });
  }
}

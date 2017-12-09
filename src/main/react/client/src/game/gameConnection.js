import Connection from '../util/connection.js';

export default class GameConnection extends Connection {
  constructor(gameId, playerId, authToken) {
    var url = `ws://localhost:4567/ws/game?gameId=${gameId}&playerId=${playerId}&authToken=${authToken}`;
    var name = `Game ${gameId}`;
    super(url, name);
  }

  startGame() {
    console.log("sending start event");
    this.send({
      eventType: "StartGameEvent"
    });
  }
}

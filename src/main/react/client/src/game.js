export default function Game(gameId, playerId, authToken) {

  this.data = {};

  this.ws = new WebSocket('ws://localhost:4567/ws/game' +
    '?gameId=' + gameId +
    '&playerId=' + playerId +
    '&authToken=' + authToken);

  this.ws.onopen = () => {
    console.log('Opened WebSocket connection to Game %s', gameId);
  };

  this.ws.onclose = (closeEvent) => {
    console.log('WebSocket connection to Game %s was closed. (Reason: %s)', gameId, closeEvent.reason);
  }

  this.ws.onmessage = (message) => {
    console.log(JSON.parse(message.data));
    this.data = JSON.parse(message.data).game;
  }

  this.disconnect = function() {
    this.ws.close(1000, 'User disconnect');
  }
}

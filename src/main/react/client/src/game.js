export default function Game(gameId, playerId, authToken) {
  if (typeof gameId === 'undefined') {
    throw new Error('Game constructor function missing argument "gameId"');
  }

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

  this.disconnect = function() {
    this.ws.close(1000, 'User disconnect');
  }
}

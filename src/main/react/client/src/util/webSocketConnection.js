export default class WebSocketConnection {
  constructor(playerId, authToken, targetName, path, params) {
    var url = `ws://localhost:4567/ws/${path}?playerId=${playerId}&authToken=${authToken}`;
    if (params) {
      for (var param in params) {
        url += `&${param}=${params[param]}`;
      }
    }
    this.ws = new WebSocket(url);

    this.ws.onopen = () => {
      console.log(`Opened WebSocket connection to ${targetName}`);
    };

    this.ws.onclose = (closeEvent) => {
      console.log(`WebSocket connection to ${targetName} was closed. (Reason: ${closeEvent.reason})`);
    }

    this.ws.onmessage = (message) => {
      var event = JSON.parse(message.data);
      console.log(event);
      console.log(this.eventHandlers);
      this.eventHandlers[event.eventType].forEach(handler => handler(event));
    }

    this.eventHandlers = {};
  }

  disconnect() {
    this.ws.close(1000, 'User disconnect');
  }

  send(event) {
    this.ws.send(JSON.stringify(event));
  }

  on(eventType, eventHandler) {
    if (!this.eventHandlers[eventType]) {
      this.eventHandlers[eventType] = [];
    }
    this.eventHandlers[eventType].push(eventHandler);
  }
}

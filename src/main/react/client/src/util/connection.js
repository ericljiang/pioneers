export default class Connection {
  constructor(webSocketURL, targetName) {
    this.eventHandlers = {};

    this.ws = new WebSocket(webSocketURL);

    this.ws.onopen = () => {
      console.log(`Opened WebSocket connection to ${targetName}`);
    };

    this.ws.onclose = (closeEvent) => {
      console.log(`WebSocket connection to ${targetName} was closed. (Reason: ${closeEvent.reason})`);
    }

    this.ws.onmessage = (message) => {
      var event = JSON.parse(message.data);
      console.log(event);
      var eventHandler = this.eventHandlers[event.eventType];
      if (eventHandler) {
        eventHandler(event);
      }
    }
  }

  disconnect() {
    this.ws.close(1000, 'User disconnect');
  }

  on(eventType, eventHandler) {
    this.eventHandlers[eventType] = eventHandler;
  }

  send(event) {
    this.ws.send(JSON.stringify(event));
  }
}

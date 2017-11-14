import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import getGreeting from './settlersClient.js';

class App extends Component {
  constructor() {
    super();
    var ws = new WebSocket("ws://localhost:4567/ws/lobby?playerId=1");
    ws.onmessage = m => console.log(m);
    this.state = { ws: ws }
  }
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <p className="App-intro">
          To get started, edit <code>src/App.js</code> and save to reload.
        </p>
        <Message />
      </div>
    );
  }
}

class Message extends Component {
  constructor() {
    super();
    this.state = { message: '' };
  }

  componentWillMount() {
    getGreeting()
      .then(data => this.setState({ message: data.greeting }));
  }

  render() {
    return (
      <p>{this.state.message}</p>
    );
  }
}

export default App;

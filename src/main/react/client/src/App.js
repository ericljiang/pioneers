import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import getGreeting from './settlersClient.js';

class App extends Component {
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
        <Lobby />
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

class Lobby extends Component {
  constructor() {
    super();
    this.state = { games: [] };
  }

  componentWillMount() {
    var ws = new WebSocket("ws://localhost:4567/ws/lobby?playerId=1");
    ws.onmessage = m => {
      var data = JSON.parse(m.data);
      console.log(data);
      this.setState({ games: data.games });
    };
  }

  render() {
    return (
      <div>
        {this.state.games.map((game, index) =>
          <p>{game.name}</p>
        )}
      </div>
    );
  }
}

export default App;

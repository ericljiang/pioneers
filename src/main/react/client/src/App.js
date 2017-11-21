import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import getGreeting from './settlersClient.js';
import { lobby } from './lobby.js';

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
    this.createGame = this.createGame.bind(this);
    this.state = { games: {} };
  }

  componentWillMount() {
    lobby.onMessage(data => this.setState({ games: data.games }));
  }

  createGame() {
    lobby.createGame("foo");
  }

  render() {
    return (
      <div>
        {Object.entries(this.state.games).map(gameEntry =>
          <p>{gameEntry[0]} {gameEntry[1].name}</p>
        )}
        <button onClick={this.createGame}>Create game</button>
      </div>
    );
  }
}

export default App;

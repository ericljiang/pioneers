import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Link,
  Redirect
} from 'react-router-dom'
import logo from './logo.svg';
import './App.css';
import getGreeting from './settlersClient.js';
import { lobby } from './lobby.js';

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <h1 className="App-title">Welcome to React</h1>
          </header>
          <Link to="/message">Message</Link>
          <Route exact path="/" render={() => (
            <Redirect to="/lobby" />
          )} />
          <Route path="/message" component={Message} />
          <Route path="/lobby" component={Lobby} />
        </div>
      </Router>
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
    this.state = { games: {} };
  }

  componentWillMount() {
    lobby.onMessage(data => this.setState({ games: data.games }));
  }

  render() {
    return (
      <div>
        <CreateGameForm />
        {Object.entries(this.state.games).map(gameEntry =>
          <Game id={gameEntry[0]} game={gameEntry[1]} />
        )}
      </div>
    );
  }
}

class CreateGameForm extends Component {
  constructor() {
    super();
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(event) {
    lobby.createGame(this.input.value);
    event.preventDefault();
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          Name:
          <input type="text" ref={(input) => this.input = input} />
        </label>
        <input type="submit" value="Create game" />
      </form>
    )
  }
}

function Game(props) {
  return (
    <div>
      {props.id} {props.game.name}
      <Link to="/message">Play</Link>
    </div>
  );
}

export default App;

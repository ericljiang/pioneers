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
import Lobby from './lobby.js';
import Game from './game.js';

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
          <Route path="/lobby" component={LobbyView} />
          <Route path="/game/:id" component={GameView} />
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

class LobbyView extends Component {
  constructor() {
    super();
    this.state = { lobby: null, games: [] };
  }

  componentWillMount() {
    var lobby = new Lobby();
    lobby.onMessage(data => this.setState({ games: data.games }));
    this.setState({ lobby: lobby });
  }

  render() {
    return (
      <div>
        <CreateGameForm lobby={this.state.lobby} />
        {Object.entries(this.state.games).map(gameEntry =>
          <GameSummary id={gameEntry[0]} game={gameEntry[1]} />
        )}
      </div>
    );
  }
}

class CreateGameForm extends Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(event) {
    this.props.lobby.createGame(this.input.value);
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

function GameSummary(props) {
  var id = props.id;
  var name = props.game.name;
  return (
    <div>
      {id}: {name}
      <Link to={"/game/" + id}>Play</Link>
    </div>
  );
}

class GameView extends Component {
  constructor() {
    super();
    this.state = { game: null };
  }

  componentWillMount() {
    var game = new Game(this.props.match.params.id);
    this.setState({ game: game });
  }

  render() {
    return <p>{this.props.match.params.id}</p>;
  }
}

export default App;

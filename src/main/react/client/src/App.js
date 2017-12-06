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
import LobbyConnection from './lobby.js';
import GameConnection from './game.js';

class App extends Component {
  constructor(props) {
    super(props);
    this.onSignIn = this.onSignIn.bind(this);
    this.state = { playerId: null, authToken: null };
  }

  onSignIn(playerId, authToken) {
    this.setState({ playerId: playerId, authToken: authToken });
  }

  render() {
    return (
      <Router>
        <div className="App">
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <h1 className="App-title">Welcome to React</h1>
          </header>

          <Link to="/message">Message</Link>

          <AuthContainer onSignIn={this.onSignIn}>
            <Route exact path="/" render={() => (
              <Redirect to="/lobby" />
            )} />
            <Route path="/message" component={Message} />
            <Route path="/lobby" render={(props) => (
              <LobbyView {...props} playerId={this.state.playerId} authToken={this.state.authToken} />
            )} />
            <Route path="/game/:id" render={(props) => (
              <GameView {...props} playerId={this.state.playerId} authToken={this.state.authToken} />
            )} />
          </AuthContainer>
        </div>
      </Router>
    );
  }
}

class AuthContainer extends Component {
  constructor(props) {
    super(props);
    this.onSignIn = this.onSignIn.bind(this);
    this.state = { signedIn: false };
  }

  onSignIn(playerId, authToken) {
    this.props.onSignIn(playerId, authToken);
    this.setState({ signedIn: true });
  }

  render() {
    if (this.state.signedIn) {
      return this.props.children;
    } else {
      return <SignInPage onSignIn={this.onSignIn} />
    }
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

class SignInPage extends Component {
  constructor(props) {
    super(props);
    this.onSignIn = this.onSignIn.bind(this);
  }

  onSignIn(googleUser) {
    this.props.onSignIn(googleUser.getBasicProfile().getId(), googleUser.getAuthResponse().id_token);
  }

  componentDidMount() {
    window.gapi.signin2.render('g-signin2', {
      'scope': 'profile email',
      'width': 240,
      'height': 50,
      'longtitle': true,
      'theme': 'dark',
      'onsuccess': this.onSignIn
    });
  }

  render() {
    return (
     <div id="g-signin2"></div>
    );
  }
}

class LobbyView extends Component {
  constructor(props) {
    super(props);
    this.state = {
      lobbyConnection: null,
      games: []
    };
  }

  componentWillMount() {
    var lobbyConnection = new LobbyConnection(this.props.playerId, this.props.authToken);
    lobbyConnection.on("LobbyUpdateEvent", event => this.setState({ games: event.games }));
    this.setState({ lobbyConnection: lobbyConnection });
  }

  componentWillUnmount() {
    this.state.lobbyConnection.disconnect();
  }

  render() {
    return (
      <div>
        <CreateGameForm playerId={this.props.playerId} onSubmit={this.state.lobbyConnection.createGame} />
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
    this.props.onSubmit(this.props.playerId, this.input.value);
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
  constructor(props) {
    super(props);
    this.state = {
      gameConnection: null,
      game: null
    };
  }

  componentWillMount() {
    var gameConnection = new GameConnection(this.props.match.params.id, this.props.playerId, this.props.authToken);
    gameConnection.on("GameUpdateEvent", (event) => {
      this.setState({ game: event.game });
      console.log("Updated game state", this.state.game);
    });
    this.setState({ gameConnection: gameConnection });
  }

  componentWillUnmount() {
    this.state.gameConnection.disconnect();
  }

  render() {
    if (this.state.game) {
      return (
        <div>
          <p>{this.state.game.id}</p>
          <p>{this.state.game.owner}</p>
          <p>{this.state.game.name}</p>
        </div>
      );
    } else {
      return <div>Loading...</div>
    }
  }
}

export default App;

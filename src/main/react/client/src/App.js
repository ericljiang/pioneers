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
    this.state = { lobby: null, games: [] };
  }

  componentWillMount() {
    var lobby = new Lobby(this.props.playerId, this.props.authToken);
    lobby.onMessage(data => this.setState({ games: data.games }));
    this.setState({ lobby: lobby });
  }

  componentWillUnmount() {
    this.state.lobby.disconnect();
  }

  render() {
    return (
      <div>
        <CreateGameForm playerId={this.props.playerId} lobby={this.state.lobby} />
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
    this.props.lobby.createGame(this.props.playerId, this.input.value);
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
    this.state = { game: null };
  }

  componentWillMount() {
    var game = new Game(this.props.match.params.id, this.props.playerId, this.props.authToken);
    this.setState({ game: game });
  }

  componentWillUnmount() {
    this.state.game.disconnect();
  }

  render() {
    return (
      <div>
        <p>{this.props.match.params.id}</p>
      </div>
    );
  }
}

export default App;

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
import AuthContainer from './auth/AuthContainer.js'
import LobbyView from './lobby/LobbyView.js'
import GameView from './game/GameView.js';

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

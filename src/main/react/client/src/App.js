import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Link,
  Redirect
} from 'react-router-dom'
import logo from './logo.svg';
import './App.css';
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
          <AuthContainer onSignIn={this.onSignIn}>
            <Route exact path="/" render={() => (
              <Redirect to="/lobby/open-games" />
            )} />
            <Route exact path="/lobby" render={() => (
              <Redirect to="/lobby/open-games" />
            )} />
            <Route path="/lobby/:view" render={(props) => (
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

export default App;

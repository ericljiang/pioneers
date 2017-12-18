import React, { Component } from 'react';
import CreateGameForm from './CreateGameForm.js';
import GameSummary from './GameSummary.js';
import LobbyConnection from './lobbyConnection.js';
import UserInfo from'./UserInfo.js';

export default class LobbyView extends Component {
  constructor(props) {
    super(props);
    this.state = {
      lobbyConnection: null,
      games: [],
      displayName: ''
    };
  }

  componentWillMount() {
    var lobbyConnection = new LobbyConnection(this.props.playerId, this.props.authToken);
    lobbyConnection.on("LobbyUpdateEvent", event => {
      this.setState({ games: event.games });
    });
    lobbyConnection.on("PlayerNameChangeEvent", event => {
      if (event.playerId == this.props.playerId) {
        this.setState({ displayName: event.displayName });
      }
    });
    this.setState({ lobbyConnection: lobbyConnection });
  }

  componentWillUnmount() {
    this.state.lobbyConnection.disconnect();
  }

  render() {
    return (
      <div>
        <header>
          <UserInfo displayName={this.state.displayName} onNameChange={this.state.lobbyConnection.changeDisplayName} />
        </header>

        <CreateGameForm playerId={this.props.playerId} onSubmit={this.state.lobbyConnection.createGame} />

        <h2>Your games</h2>
        <table>
          {this.state.games
            .filter(game => !game.pregame)
            .filter(game => game.players.hasOwnProperty(this.props.playerId))
            .map(game =>
              <GameSummary game={game} />
            )}
        </table>

        <h2>Open games</h2>
        <table>
          {this.state.games
            .filter(game => game.pregame)
            .map(game =>
              <GameSummary game={game} />
            )}
        </table>
      </div>
    );
  }
}

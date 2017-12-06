import React, { Component } from 'react';
import CreateGameForm from './CreateGameForm.js';
import GameSummary from './GameSummary.js';
import LobbyConnection from './lobbyConnection.js';

export default class LobbyView extends Component {
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

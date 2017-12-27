import React, { Component } from 'react';
import { Redirect } from 'react-router-dom'
import { withStyles } from 'material-ui/styles';
import Button from 'material-ui/Button';
import AddIcon from 'material-ui-icons/Add';
import LobbyConnection from './lobbyConnection.js';
import GameCreationDialog from './GameCreationDialog.js';
import LobbyHeader from './LobbyHeader.js';
import GameList from './GameList.js';

const styles = {
  button: {
    position: "fixed",
    bottom: "20px",
    right: "20px"
  }
}

class LobbyView extends Component {
  constructor(props) {
    super(props);
    this.state = {
      lobbyConnection: null,
      games: [],
      displayName: '',
      gameCreationDialogOpen: false
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

  openGameCreationDialog = () => {
    this.setState({ gameCreationDialogOpen: true });
  }

  closeGameCreationDialog = () => {
    this.setState({ gameCreationDialogOpen: false });
  }

  render() {
    const view = this.props.match.params.view;
    var games;
    if (view === "open-games") {
      games = this.state.games
        .filter(game => game.pregame);
    } else if (view === "your-games") {
      games = this.state.games
        .filter(game => !game.pregame)
        .filter(game => game.players.hasOwnProperty(this.props.playerId));
    } else {
      return (
        <Redirect to="/lobby/open-games" />
      );
    }

    return (
      <div>
        <LobbyHeader
          displayName={this.state.displayName}
          onNameChange={this.state.lobbyConnection.changeDisplayName}
          view={view} />
        <GameList games={games} />
        <Button
          className={this.props.classes.button}
          fab
          color="primary"
          aria-label="add"
          onClick={this.openGameCreationDialog}>
          <AddIcon />
        </Button>
        <GameCreationDialog
          open={this.state.gameCreationDialogOpen}
          playerId={this.props.playerId}
          onClose={this.closeGameCreationDialog}
          onSubmit={this.state.lobbyConnection.createGame} />
      </div>
    );
  }
}

export default withStyles(styles)(LobbyView);

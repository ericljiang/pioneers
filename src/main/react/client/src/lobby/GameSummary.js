import React, { Component } from 'react';
import { withRouter } from 'react-router-dom'
import { withStyles } from 'material-ui/styles';
import { TableCell, TableRow } from 'material-ui/Table';

const styles = {
  row: {
    cursor: 'pointer'
  }
};

class GameSummary extends Component {
  constructor(props) {
    super(props);
  }

  playersOnline(players) {
    return Object.keys(players)
      .filter(playerId => players[playerId].online)
      .length;
  }

  joinGame = () => {
    this.props.history.push(`/game/${this.props.game.id}`);
  }

  render() {
    return (
      <TableRow hover className={this.props.classes.row} onClick={this.joinGame}>
        <TableCell>{this.props.game.name}</TableCell>
        <TableCell>
          {this.playersOnline(this.props.game.players)}/{this.props.game.pregame ? this.props.game.maximumPlayers : Object.keys(this.props.game.players).length}
        </TableCell>
      </TableRow>
    );
  }
}

export default withStyles(styles)(withRouter(GameSummary));

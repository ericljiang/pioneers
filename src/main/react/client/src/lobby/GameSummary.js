import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default function GameSummary(props) {
  var name = props.game.name;
  return (
    <tr>
      <td>{props.game.id}</td>
      <td>{props.game.name}</td>
      <td>{playersOnline(props.game.players)}/{props.game.pregame ? props.game.maximumPlayers : Object.keys(props.game.players).length}</td>
      <td><Link to={"/game/" + props.game.id}>Play</Link></td>
    </tr>
  );
}

function playersOnline(players) {
  return Object.keys(players)
    .filter(playerId => players[playerId].online)
    .length;
}

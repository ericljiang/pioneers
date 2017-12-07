import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default function GameSummary(props) {
  var name = props.game.name;
  return (
    <tr>
      <td>{props.game.id}</td>
      <td>{props.game.name}</td>
      <td>{props.game.players.length} players online</td>
      <td><Link to={"/game/" + props.game.id}>Play</Link></td>
    </tr>
  );
}

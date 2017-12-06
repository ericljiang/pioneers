import React, { Component } from 'react';
import { Link } from 'react-router-dom'

export default function GameSummary(props) {
  var id = props.id;
  var name = props.game.name;
  return (
    <div>
      {id}: {name}
      <Link to={"/game/" + id}>Play</Link>
    </div>
  );
}

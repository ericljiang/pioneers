import React, { Component } from 'react';

export default class UserInfo extends Component {
  constructor(props) {
    super(props);
    this.handleNameChange = this.handleNameChange.bind(this);
  }

  handleNameChange(event) {
    this.props.onNameChange(this.input.value);
    event.preventDefault();
  }

  render() {
    return (
      <form onSubmit={this.handleNameChange}>
        {this.props.displayName}
        <input type="text" ref={(input) => this.input = input} />
        <input type="submit" value="Submit" />
      </form>
    )
  }
}

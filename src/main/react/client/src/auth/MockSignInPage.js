import React, { Component } from 'react';

export default class MockSignInPage extends Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.onSignIn("1", "1");
  }

  render() {
    return (
      <p>Mock sign in page</p>
    );
  }
}

import React, { Component } from 'react';
import SignInPage from './SignInPage.js'
// import MockSignInPage from './MockSignInPage.js'

export default class AuthContainer extends Component {
  constructor(props) {
    super(props);
    this.onSignIn = this.onSignIn.bind(this);
    this.state = { signedIn: false };
  }

  onSignIn(playerId, authToken) {
    this.props.onSignIn(playerId, authToken);
    this.setState({ signedIn: true });
  }

  render() {
    if (this.state.signedIn) {
      return this.props.children;
    } else {
      return <SignInPage onSignIn={this.onSignIn} />
      // return <MockSignInPage onSignIn={this.onSignIn} />
    }
  }
}

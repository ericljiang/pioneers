import React, { Component } from 'react';

export default class SignInPage extends Component {
  constructor(props) {
    super(props);
    this.onSignIn = this.onSignIn.bind(this);
  }

  onSignIn(googleUser) {
    this.props.onSignIn(googleUser.getBasicProfile().getId(), googleUser.getAuthResponse().id_token);
  }

  componentDidMount() {
    window.gapi.signin2.render('g-signin2', {
      'scope': 'profile email',
      'width': 240,
      'height': 50,
      'longtitle': true,
      'theme': 'dark',
      'onsuccess': this.onSignIn
    });
  }

  render() {
    return (
      <div id="g-signin2"></div>
    );
  }
}

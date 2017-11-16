import React, { Component } from 'react';
import TwitterLogin from 'react-twitter-auth/lib/react-twitter-auth-component';
import { Link } from 'react-router-dom';
import Firebase, { auth, provider } from '../config/firebase';

let appToken;

const db = Firebase.database();

export default class Login extends Component {
  login = () => {
    auth
      .signInWithPopup(provider)
      .then(res => {
        const user = res.user;
        const accessToken = res.credential.accessToken;
        const accessTokenSecret = res.credential.secret;
        this.props.handleUserLogin(user, accessToken, accessTokenSecret);
      })
      .catch(err => {
        console.log(err);
      });
  };
  render() {
    return (
      <div>
        <h1>Login Page</h1>
        <button onClick={this.login}>Log In</button>
      </div>
    );
  }
}

import React, {Component} from 'react';
import {Button, Icon} from 'semantic-ui-react';
import {Link} from 'react-router-dom';
import styled from 'styled-components';

import styles from '../scss/Login.module.scss';
import NavBar from './NavBar';
import {auth, provider, database} from '../config/firebase';

let appToken;

const Background = styled.div`
  background-image: url('https://www.untapt.com/industry/wp-content/uploads/2015/11/twitterlogo.png');
  width: 100vw;
  height: 100vh;
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
`;

const Title = styled.h1`
  color: white;
  font-family: 'Roboto', sans-serif;
`;

const Content = styled.h3`
  font-family: 'Roboto', sans-serif;
  margin-top: 40px;
  color: white;
`;

const TextContainer = styled.div`
  position: absolute;
  top: 40%;
  left: 20%;
`;

export default class Login extends Component {
  login = () => {
    auth
      .signInWithPopup(provider)
      .then(res => {
        const username = res.additionalUserInfo.username;
        localStorage.setItem('username', username);
        const user = res.user;
        localStorage.setItem('uid', user.uid);
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
      <Background>
        <TextContainer>
          <Title>Twitter Cleaner</Title>
          <Content>
            Twitter Cleaner suggests Twitter users for you to follow and
            unfollow<br />
            based on your defined preferences.
            <p>To continue, please login with your Twitter Account.</p>
          </Content>
          <Button onClick={this.login} className={styles.iconButton}>
            <Icon name="twitter" size="large" className={styles.twitterIcon} />Log
            In
          </Button>
        </TextContainer>
      </Background>
    );
  }
}

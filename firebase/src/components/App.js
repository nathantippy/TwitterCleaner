import React, { Component } from 'react';
import { Container } from 'reactstrap';
import { withRouter, Route, Switch, Redirect } from 'react-router-dom';
import axios from 'axios';
import isEqual from 'lodash/isEqual';

import styles from '../scss/App.module.scss';
import Loading from './Loading';
import NavBar from './NavBar';
import Main from './Main';
import SettingsPage from './SettingsPage';
import Login from './Login';
import { auth } from './config/firebase';

class App extends Component {
  state = {
    accounts: [],
    isFollow: true,
    user: null,
    loaded: false,
    isFetching: true
  };
  getFollow = () => {
    let follow;
    if (this.state.isFollow) {
      follow = 'follow';
    } else {
      follow = 'unfollow';
    }
    return follow;
  };

  getUsername = () => {
    return localStorage.getItem('username');
  };
  getTwitterAccounts = () => {
    const follow = this.getFollow();
    const username = "rojowolf21"; //this.getUsername();
    axios
      .get(
        `https://twittercleaner-7476e.firebaseio.com/users/${username}/${
          follow
        }s.json`
      )
      .then(res => {
        this.setState({ accounts: res.data, isFetching: false });
      })
      .catch(err => {
        console.log(err);
      });
  };

  handleUserLogin = (user, accessToken, accessTokenSecret) => {
    this.setState({
      user: user,
      accessToken: accessToken,
      accessTokenSecret: accessTokenSecret
    });
    this.getTwitterAccounts();
  };

  handleUserLogout = () => {
    auth.signOut().then(() => {
      this.setState({ user: null, accessToken: '', accessTokenSecret: '' });
    });
  };

  handleRemove = userid => {
    let newAccounts = this.state.accounts.slice();
    const index = newAccounts.findIndex(a => a.userid === userid);
    newAccounts.splice(index, 1);
    this.setState({ accounts: newAccounts });
  };
  toggleClick = () => {
    this.setState({
      isFollow: !this.state.isFollow
    });
  };
  componentWillMount() {
    auth.onAuthStateChanged(user => {
      if (user) {
        console.log(user);
        this.setState({ user, loaded: true });
      } else {
        this.setState({ loaded: true });
      }
    });
  }

  componentDidMount() {
    if (localStorage.getItem('username') !== null) {
      this.getTwitterAccounts();
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (prevState.isFollow !== this.state.isFollow) {
      this.setState({ isFetching: true });
      this.props.history.push(`${this.getFollow()}`);
      this.getTwitterAccounts();
    }
  }

  render() {
    const { accounts, isFollow, isFetching } = this.state;
    if (!this.state.loaded) return <Loading />;
    if (this.state.user === null || this.state.username === null) {
      return (
        <div>
          <Redirect from="/" to="/login" />
          <Route
            exact={true}
            path="/login"
            render={props => (
              <Login {...props} handleUserLogin={this.handleUserLogin} />
            )}
          />
        </div>
      );
    } else {
      return (
        <div className={styles.App}>
          <Container>
            <NavBar
              handleUserLogout={this.handleUserLogout}
              user={this.state.user}
            />
            <Switch>
              <Route path="/settings" component={SettingsPage} />
              <Route
                exact={true}
                path="/follow"
                render={props => (
                  <Main
                    {...props}
                    toggle={this.toggleClick}
                    isFetching={isFetching}
                    accounts={accounts}
                    isFollow={isFollow}
                    handleRemove={this.handleRemove}
                  />
                )}
              />
              <Route
                exact={true}
                path="/unfollow"
                render={props => (
                  <Main
                    {...props}
                    toggle={this.toggleClick}
                    isFetching={isFetching}
                    accounts={accounts}
                    isFollow={isFollow}
                    handleRemove={this.handleRemove}
                  />
                )}
              />
              <Redirect from="/" to="/follow" />
            </Switch>
          </Container>
        </div>
      );
    }
  }
}
export default withRouter(App);

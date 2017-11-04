import React, { Component } from 'react';
import { Container } from 'reactstrap';
import { Route } from 'react-router-dom';
import axios from 'axios';

import styles from '../scss/App.module.scss';
import NavBar from './NavBar';
import Main from './Main';
import SettingsPage from './SettingsPage';

class App extends Component {
  state = {
    accounts: [],
    isFollow: true,
    user: {
      name: 'Dennis Bouvier',
      username: 'dr_b',
      tweets: '200',
      followers: '450',
      following: '800'
    }
  };
  getTwitterAccounts = () => {
    let follow;
    if (this.state.isFollow) {
      follow = 'follow';
    } else {
      follow = 'unfollow';
    }
    axios.get(`http://localhost:3000/${follow}`).then(res => {
      const twitterAccounts = res.data.accounts;
      this.setState({ accounts: twitterAccounts });
    });
  };
  handleRemove = i => {
    let newAccounts = this.state.accounts.slice();
    newAccounts.splice(i, 1);
    this.setState({ accounts: newAccounts });
  };
  toggleClick = () => {
    this.setState({
      isFollow: !this.state.isFollow
    });
  };
  componentDidMount() {
    this.getTwitterAccounts();
    setInterval(() => this.getTwitterAccounts(), 10000);
  }

  componentDidUpdate(prevProps, prevState) {
    if (prevState.isFollow !== this.state.isFollow) {
      this.getTwitterAccounts();
    }
  }
  render() {
    const { accounts, isFollow } = this.state;
    return (
      <div className={styles.App}>
        <Container>
          <NavBar />
          <Route path="/settings" component={SettingsPage} />
          <Route
            exact={true}
            path="/"
            render={props => (
              <Main
                {...props}
                toggle={this.toggleClick}
                accounts={accounts}
                isFollow={isFollow}
                handleRemove={this.handleRemove}
              />
            )}
          />
        </Container>
      </div>
    );
  }
}
export default App;

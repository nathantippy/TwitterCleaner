import React, { Component } from 'react';
import { CSSTransitionGroup } from 'react-transition-group';

import TwitterAccount from './TwitterAccount';

import '../scss/TwitterAccountList.scss';

export default class TwitterAccountList extends Component {
  render() {
    const accounts = this.props.accounts.map((account, i) => (
      <TwitterAccount
        key={account.username}
        username={account.username}
        name={account.name}
        created={new Date(account.created)}
        reason={account.reason}
        isFollow={this.props.isFollow}
        handleRemove={this.props.handleRemove.bind(this, i)}
        className="account"
      />
    ));
    console.log(accounts);
    if (this.props.isFollow === true) {
      accounts.sort(
        (a, b) => b.props.created.getTime() - a.props.created.getTime()
      );
    } else {
      accounts.sort(
        (a, b) => a.props.created.getTime() - b.props.created.getTime()
      );
    }
    return (
      <div>
        <CSSTransitionGroup
          transitionName="tweet"
          transitionEnterTimeout={500}
          transitionLeaveTimeout={300}
        >
          {accounts}
        </CSSTransitionGroup>
      </div>
    );
  }
}

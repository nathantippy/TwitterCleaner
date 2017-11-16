import React, { Component } from 'react';
import { CSSTransitionGroup } from 'react-transition-group';

import TwitterAccount from './TwitterAccount';

import '../scss/TwitterAccountList.scss';

export default class TwitterAccountList extends Component {
  render() {
    const accounts = this.props.accounts.map((account, i) => (
      <TwitterAccount
        key={account.userid}
        userid={account.userid}
        username={account.screenname}
        name={account.username}
        reason={account.reason}
        isFollow={this.props.isFollow}
        handleRemove={this.props.handleRemove.bind(this, account.userid)}
        className="account"
      />
    ));
    return (
      <div>
        <CSSTransitionGroup
          transitionName="tweet"
          transitionEnterTimeout={500}
          transitionLeaveTimeout={300}>
          {accounts}
        </CSSTransitionGroup>
      </div>
    );
  }
}

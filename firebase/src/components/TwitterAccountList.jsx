import React, { Component } from 'react';
import { CSSTransitionGroup } from 'react-transition-group';
import { Scrollbars } from 'react-custom-scrollbars';

import TwitterAccount from './TwitterAccount';

import styles from '../scss/TwitterAccountList.scss';

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
        className={styles.account}
      />
    ));
    return (
      <Scrollbars autoHeight={true} autoHeightMin={1200} autoHeightMax={1300}>
        <CSSTransitionGroup
          transitionName="tweet"
          transitionEnterTimeout={500}
          transitionLeaveTimeout={300}>
          {accounts}
        </CSSTransitionGroup>
      </Scrollbars>
    );
  }
}

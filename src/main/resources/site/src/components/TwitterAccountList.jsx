import React, {Component} from 'react';
import {CSSTransitionGroup} from 'react-transition-group';
import {List, ListItem} from 'material-ui/List';
import Divider from 'material-ui/Divider';
import {Scrollbars} from 'react-custom-scrollbars';
import Subheader from 'material-ui/Subheader';
import Avatar from 'material-ui/Avatar';
import RaisedButton from 'material-ui/RaisedButton';

import TwitterAccount from './TwitterAccount';

import '../scss/TwitterAccountList.scss';

export default class TwitterAccountList extends Component {
  render() {
    const follow = this.props.isFollow;
    const style = {
      margin: 5
    };
    let accounts;
    if (this.props.accounts) {
      accounts = this.props.accounts.map((account, i) => (
        <div>
          <ListItem
            leftAvatar={<Avatar src={account.avatar} />}
            key={account.userid}
            userid={account.userid}
            secondaryText={
              <div>
                <p style={{lineHeight: 1}}>
                  @{account.screenname}{' '}
                  <span style={{marginLeft: 12}}>Reason: {account.reason}</span>
                </p>
              </div>
            }
            primaryText={account.username}
            isFollow={this.props.isFollow}
            handleRemove={this.props.handleRemove.bind(this, account.userid)}>
            <RaisedButton
              label={follow === true ? 'Follow' : 'Unfollow'}
              primary={true}
              style={style}
            />
            <RaisedButton
              label={follow === true ? 'Never Follow' : 'Never Unfollow'}
              secondary={true}
              style={style}
            />
          </ListItem>
          <Divider />
        </div>
      ));
    }
    return (
      <Scrollbars autoHeight={true} autoHeightMin={1200} autoHeightMax={1300}>
        <CSSTransitionGroup
          transitionName="tweet"
          transitionEnterTimeout={500}
          transitionLeaveTimeout={300}>
          <List>
            <Subheader inset={true}>Accounts</Subheader>
            {accounts}
          </List>
        </CSSTransitionGroup>
      </Scrollbars>
    );
  }
}

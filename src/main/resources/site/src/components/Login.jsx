import React, { Component } from 'react';
import { Button, Icon } from 'semantic-ui-react';
import { Link } from 'react-router-dom';

import styles from '../scss/Login.module.scss';
import NavBar from './NavBar';
import { auth, provider, database } from '../config/firebase';

let appToken;

export default class Login extends Component {
	login = () => {
		auth
			.signInWithPopup(provider)
			.then(res => {
				const username = res.additionalUserInfo.username;
				localStorage.setItem('username', username);
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
			<div className={styles.body}>
				<NavBar />
				<img
					className={styles.tweetImage}
					src="http://www.mobyaffiliates.com/wp-content/uploads/2017/11/twitter-down.png"
				/>
				<div className={styles.content}>
					<h1>Welcome to Twitter Cleaner!</h1>
					<br />
					<br />
					<h3>
						Twitter Cleaner suggests Twitter users for you to follow and
						unfollow<br />
						based on your defined preferences.
					</h3>
					<p>To continue, please login with your Twitter Account.</p>
					<Button onClick={this.login} className={styles.iconButton}>
						<Icon name="twitter" size="large" className={styles.twitterIcon} />Log
						In
					</Button>
				</div>
			</div>
		);
	}
}

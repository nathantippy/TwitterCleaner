import React, { Component } from "react";
import { Icon } from "semantic-ui-react";
import { Link } from "react-router-dom";

import styles from "../scss/SettingsPage.module.scss";

export default class SettingsPage extends Component {
  render() {
    return (
      <div className={styles.container}>
        <Link to="/" className={styles.backButton}>
          <Icon name="chevron left" size="large" />
          <span>Back</span>
        </Link>
        <div className={styles.flexContainer}>
          <div className={styles.region}>Region</div>
          <div className={styles.unfollowRegion}>Unfollow By Region</div>
          <div className={styles.language}>Language</div>
          <div className={styles.unfollowLanguage}>Unfollow By Language</div>
          <div className={styles.nsfw}>NSFW</div>
          <div className={styles.unfollownsfw}>Unfollow By NSFW</div>
          <div className={styles.nDays}>Unfollow Those after N Days</div>
          <div className={styles.followBack}>Follow Back</div>
        </div>
      </div>
    );
  }
}

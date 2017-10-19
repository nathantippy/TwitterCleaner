import React, { Component } from "react";
import { Icon } from "semantic-ui-react";
import { Link } from "react-router-dom";

import Modal from "./Modal";
import styles from "../scss/SettingsPage.module.scss";

export default class SettingsPage extends Component {
  state = {
    activeModal: null
  };

  openModal(modal) {
    this.setState({ activeModal: modal });
  }

  closeModal() {
    this.setState({ activeModal: null });
  }

  render() {
    return (
      <div className={styles.container}>
        <Link to="/" className={styles.backButton}>
          <Icon name="chevron left" size="large" />
          <span>Back</span>
        </Link>
        <Modal
          isOpen={this.state.activeModal === "region"}
          onClose={() => this.closeModal()}
        >
          <h1>Region</h1>
          <button onClick={() => this.closeModal()}>Close</button>
        </Modal>
        <Modal
          isOpen={this.state.activeModal === "language"}
          onClose={() => this.closeModal()}
        >
          <h1>Language</h1>
          <button onClick={() => this.closeModal()}>Close</button>
        </Modal>
        <Modal
          isOpen={this.state.activeModal === "nsfw"}
          onClose={() => this.closeModal()}
        >
          <h1>NSFW</h1>
          <button onClick={() => this.closeModal()}>Close</button>
        </Modal>
        <div className={styles.flexContainer}>
          <a className={styles.region} onClick={() => this.openModal("region")}>
            <div>Region Settings</div>
          </a>
          <a
            className={styles.language}
            onClick={() => this.openModal("language")}
          >
            Language Settings
          </a>
          <a className={styles.nsfw} onClick={() => this.openModal("nsfw")}>
            NSFW Settings
          </a>
          <div className={styles.nDays}>Unfollow Those after N Days</div>
        </div>
      </div>
    );
  }
}

import React, { Component } from 'react';
import { Button, Icon } from 'semantic-ui-react';

import Modal from './Modal';
import styles from '../scss/Options.module.scss';
export default class Options extends Component {
  state = {
    isModal: false
  };
  openModal() {
    this.setState({ isModal: true });
  }
  closeModal() {
    this.setState({ isModal: false });
  }
  render() {
    return (
      <div>
        <Modal isOpen={this.state.isModal}>
          <h1 className={styles.title}>Choose a Filter:</h1>
          <form>
            <input type="checkbox" id="delete" name="remove" value="delete" />
            <label className={styles.label} for="delete">
              Remove all suggestions older than a day
            </label>
            <br />
            <input type="checkbox" id="sort" name="sort" value="sort" />
            <label className={styles.label} for="sort">
              Sort suggestions by reasons
            </label>
          </form>
          <button className={styles.update} onClick={() => this.closeModal()}>
            Update
          </button>
          <button className={styles.cancel} onClick={() => this.closeModal()}>
            Cancel
          </button>
        </Modal>
        <Button className={styles.iconButton} onClick={() => this.openModal()}>
          <Icon name="filter" size="large" className={styles.bigIcon} />
          Filter Options
        </Button>
      </div>
    );
  }
}

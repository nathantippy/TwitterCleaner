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
          Hello
          <button onClick={() => this.closeModal()}>Close</button>
        </Modal>
        <Button className={styles.options} onClick={() => this.openModal()}>
          Filter Options
        </Button>
      </div>
    );
  }
}

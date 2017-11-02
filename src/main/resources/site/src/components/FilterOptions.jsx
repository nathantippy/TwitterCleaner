import React, { Component } from "react";
import Modal from "./Modal";
import styles from "../scss/Options.module.scss";
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
        <a className={styles.options} onClick={() => this.openModal()}>
          Options
        </a>
      </div>
    );
  }
}

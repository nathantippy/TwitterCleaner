import React, { Component } from "react";
import styles from "../scss/Modal.module.scss";

export default class Modal extends Component {
  close(e) {
    e.preventDefault();

    if (this.props.onClose) {
      this.props.onClose();
    }
  }

  render() {
    const { onClose, children, isOpen } = this.props;
    if (isOpen === false) return null;

    return (
      <div>
        <div className={styles.modal}>{children}</div>
        <div className={styles.backdrop} onClick={e => this.close(e)} />
      </div>
    );
  }
}

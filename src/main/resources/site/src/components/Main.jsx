import React, { Component } from "react";
import Settings from "./Settings";
import Toggle from "./Toggle";
import Options from "./Options";
import styles from "../scss/Main.module.scss";

export default class Main extends Component {
  render() {
    return (
      <div className={styles.container}>
        <Settings />
        <Toggle {...this.props} />
        <Options />
      </div>
    );
  }
}

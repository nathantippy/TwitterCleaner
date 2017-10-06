import React, { Component } from "react";

export default class TwitterAccount extends Component {
  render() {
    console.log(this.props);
    const randomFlatColor = require("random-flat-colors");
    const styles = {
      slide: {
        minHeight: 100,
        boxSizing: "border-box",
        color: "#fff",
        paddingTop: "10px",
        textAlign: "center"
      },
      ghostButton: {
        display: "inline-block",
        padding: "8px",
        color: "#fff",
        border: "1px solid #fff",
        textAlign: "center",
        outline: "none",
        textDecoration: "none",
        backgroundColor: "transparent",
        borderRadius: "6px",
        marginRight: "20px",
        marginBottom: "10px",
        marginTop: "10px"
      }
    };
    return (
      <div>
        <div
          style={Object.assign({}, styles.slide, {
            backgroundColor: randomFlatColor([
              "blue",
              "teal",
              "green",
              "purple",
              "dark",
              "red"
            ])
          })}
        >
          <span
            style={{
              fontSize: "24px"
            }}
          >
            {this.props.name}
          </span>
          <span
            style={{
              fontSize: "16px"
            }}
          >
            {" @" + this.props.username}
          </span>
          <br />
          <span>{"Reason: " + this.props.reason}</span>
          <br />
          <button style={styles.ghostButton}>Never Unfollow</button>
          <button style={styles.ghostButton}>Unfollow</button>
        </div>
      </div>
    );
  }
}

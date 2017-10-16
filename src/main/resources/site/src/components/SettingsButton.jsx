import React, { Component } from "react";
import { Button, Icon } from "semantic-ui-react";
import { Link } from "react-router-dom";

export default class Settings extends Component {
  state = {
    width: window.innerWidth
  };

  componentWillMount() {
    window.addEventListener("resize", this.handleWindowSizeChange);
  }

  componentWillUnmount() {
    window.removeEventListener("resize", this.handleWindowSizeChange);
  }

  handleWindowSizeChange = () => {
    this.setState({ width: window.innerWidth });
  };

  render() {
    const { width } = this.state;
    const isMobile = width <= 500;
    if (isMobile) {
      return (
        <Link to="/settings">
          <Icon name="settings" size="large" />
        </Link>
      );
    } else {
      return (
        <Link to="/settings">
          <Button>
            <Icon name="settings" size="large" />
            Settings
          </Button>
        </Link>
      );
    }
  }
}

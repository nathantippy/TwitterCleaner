import React, { Component } from "react";
import { Navbar, NavItem, Nav } from "react-bootstrap";
import "../scss/NavBar.module.scss";
class NavBar extends Component {
  render() {
    return (
      <Navbar className="navigation">
        <Navbar.Header>
          <Navbar.Brand>
            <a href="#">OCI</a>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Nav pullRight>
            <NavItem href="#">Log Out</NavItem>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }
}
export default NavBar;

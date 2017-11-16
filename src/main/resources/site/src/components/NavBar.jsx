import React, { Component } from 'react';
import { Navbar, NavItem, Nav } from 'react-bootstrap';
import { auth } from '../config/firebase';
import '../scss/NavBar.scss';
class NavBar extends Component {
  render() {
    return (
      <Navbar className="navigation" fixedTop={true}>
        <Navbar.Header>
          <Navbar.Brand>
            <img src="OCILogo.png" alt="OCI Logo" />
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          {this.props.user ? (
            <div>
              <Nav pullRight={true} onSelect={this.props.handleUserLogout}>
                <NavItem>Log Out</NavItem>
              </Nav>
              <Navbar.Text>Hello, {this.props.user.displayName}</Navbar.Text>
            </div>
          ) : (
            ''
          )}
        </Navbar.Collapse>
      </Navbar>
    );
  }
}
export default NavBar;

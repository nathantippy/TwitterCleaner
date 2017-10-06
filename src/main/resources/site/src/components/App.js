import React, { Component } from "react";
import styles from "../scss/App.module.scss";
import TwitterAccountList from "./TwitterAccountList";
import NavBar from "./NavBar";
import { Container, Row, Col } from "reactstrap";
import Main from "./Main";

/*async function loadTwitterAccounts() {
  let res;
  let url = "http://beta.json-generator.com/api/json/get/415RfqPRz";
  try {
    res = await fetch(url);
    const twitterAccounts = await res.json();
    React.setState({ accounts: twitterAccounts });
  } catch (error) {}
} */
class App extends Component {
  constructor(props) {
    super(props);
    React.setState = this.setState.bind(this);
    this.state = {
      accounts: [
        {
          name: "George",
          username: "CarlLE3",
          reason: "Stinker"
        },
        {
          name: "Lenny",
          username: "TheRabbit",
          reason: "Language"
        },
        {
          name: "Ox",
          username: "Chamberlain",
          reason: "Ignoring You"
        },
        {
          name: "Quinn Vaughn",
          username: "giggity",
          reason: "Meanie"
        }
      ],
      isFollow: true,
      user: {
        name: "Dennis Bouvier",
        username: "dr_b",
        tweets: "200",
        followers: "450",
        following: "800"
      }
    };
    this.toggleClick = this.toggleClick.bind(this);
  }
  handleRemove(i) {
    let newAccounts = this.state.accounts.slice();
    newAccounts.splice(i, 1);
    React.setState({ accounts: newAccounts });
  }
  toggleClick() {
    React.setState({
      isFollow: !this.state.isFollow
    });
  }
  /*componentDidMount() {
    loadTwitterAccounts();
    setInterval(() => loadTwitterAccounts(), 10000);
  } */
  render() {
    const { isFollow } = this.state;
    return (
      <div className={styles.App}>
        <NavBar />
        <Container>
          <Main toggle={this.toggleClick} follow={isFollow} />
          <Row>
            <Col md="12" sm="12">
              <TwitterAccountList
                accounts={this.state.accounts}
                isFollow={this.state.isFollow}
                handleRemove={this.handleRemove.bind(this)}
              />
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}
export default App;

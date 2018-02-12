import React, {Component, Fragment} from 'react';
import {Row, Col} from 'reactstrap';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import Settings from 'material-ui/svg-icons/action/settings';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import {Tabs, Tab} from 'material-ui/Tabs';
import FilterList from 'material-ui/svg-icons/content/filter-list';
import SettingsPage from '../pages/Settings';
import FilterPage from '../pages/Filter';
import Loading from './Loading';

import TwitterAccountList from './TwitterAccountList';

export default class Main extends Component {
  state = {
    openSettings: false,
    openFilter: false,
    value: 'follow'
  };

  handleOpenSettings = () => {
    this.setState({openSettings: true});
  };

  handleCloseSettings = () => {
    this.setState({openSettings: false});
  };

  handleOpenFilter = () => {
    this.setState({openFilter: true});
  };

  handleCloseFilter = () => {
    this.setState({openFilter: false});
  };

  handleTabChange = value => {
    this.props.handleChange();
    this.setState({
      value: value
    });
  };

  render() {
    const actionsSettings = [
      <FlatButton
        label="Update"
        primary={true}
        onClick={this.handleCloseSettings}
      />,
      <FlatButton
        label="Close"
        secondary={true}
        onClick={this.handleCloseSettings}
      />
    ];
    const actionsFilters = [
      <FlatButton
        label="Update"
        primary={true}
        onClick={this.handleCloseFilter}
      />,
      <FlatButton
        label="Close"
        secondary={true}
        onClick={this.handleCloseFilter}
      />
    ];
    return (
      <div>
        <AppBar
          title="Twitter Cleaner"
          style={{textAlign: 'center'}}
          iconElementRight={
            <Fragment>
              <IconButton>
                <FilterList onClick={this.handleOpenFilter} color="white" />
                <Dialog
                  title="Filter Options"
                  actions={actionsFilters}
                  modal={false}
                  open={this.state.openFilter}
                  onRequestClose={this.handleCloseFilter}
                  autoDetectWindowHeight={true}
                  autoScrollBodyContent={true}
                  repositionOnUpdate={true}>
                  <FilterPage />
                </Dialog>
              </IconButton>
              <FlatButton
                label="Logout"
                style={{color: 'white'}}
                onClick={this.props.handleUserLogout}
              />
            </Fragment>
          }
          iconElementLeft={
            <IconButton>
              <Settings onClick={this.handleOpenSettings} />
              <Dialog
                title="Settings"
                actions={actionsSettings}
                modal={false}
                open={this.state.openSettings}
                onRequestClose={this.handleCloseSettings}
                autoDetectWindowHeight={true}
                autoScrollBodyContent={true}
                repositionOnUpdate={true}>
                <SettingsPage />
              </Dialog>
            </IconButton>
          }
        />
        <Tabs value={this.state.value} onChange={this.handleTabChange}>
          <Tab label="Follow" value="follow">
            <Row>
              <Col md="12" sm="12">
                {this.props.isFetching ? (
                  <Loading />
                ) : (
                  <TwitterAccountList
                    accounts={this.props.accounts}
                    isFollow={this.props.isFollow}
                    handleRemove={this.props.handleRemove.bind(this)}
                  />
                )}
              </Col>
            </Row>
          </Tab>
          <Tab label="Unfollow" value="unfollow">
            <Row>
              <Col md="12" sm="12">
                {this.props.isFetching ? (
                  <Loading />
                ) : (
                  <TwitterAccountList
                    accounts={this.props.accounts}
                    isFollow={this.props.isFollow}
                    handleRemove={this.props.handleRemove.bind(this)}
                  />
                )}
              </Col>
            </Row>
          </Tab>
        </Tabs>
      </div>
    );
  }
}

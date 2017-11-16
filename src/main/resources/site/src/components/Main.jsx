import React, { Component } from 'react';
import { Row, Col } from 'reactstrap';

import TwitterAccountList from './TwitterAccountList';
import SettingsButton from './SettingsButton';
import Toggle from './Toggle';
import FilterOptions from './FilterOptions';
import styles from '../scss/Main.module.scss';
import Fetching from './FetchingData';

export default class Main extends Component {
  render() {
    return (
      <div>
        <div className={styles.container}>
          <SettingsButton />
          <Toggle {...this.props} />
          <FilterOptions />
        </div>
        <Row>
          <Col md="12" sm="12">
            {this.props.isFetching ? (
              <Fetching />
            ) : (
              <TwitterAccountList
                accounts={this.props.accounts}
                isFollow={this.props.isFollow}
                handleRemove={this.props.handleRemove.bind(this)}
              />
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

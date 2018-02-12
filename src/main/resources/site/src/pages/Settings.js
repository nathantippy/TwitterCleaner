import React, {Component} from 'react';
import {List, ListItem} from 'material-ui/List';
import Subheader from 'material-ui/Subheader';
import Divider from 'material-ui/Divider';
import Checkbox from 'material-ui/Checkbox';
import {Tabs, Tab} from 'material-ui/Tabs';

export default class Settings extends Component {
  state = {
    languages: [
      'English',
      'Spanish',
      'Italian',
      'French',
      'Mandarin',
      'Cantonese',
      'Japanese'
    ],
    regions: [
      'North America',
      'South America',
      'Europe',
      'Asia',
      'Africa',
      'Australia'
    ],
    nsfws: [
      'Swearing',
      'Nudity',
      'Pornography',
      'Sexual Content (Talking about sex)',
      'Gross'
    ],
    misc: ['Book seller', 'Politics']
  };
  render() {
    const languages = this.state.languages.map(language => (
      <ListItem
        key={language}
        primaryText={language}
        leftCheckbox={<Checkbox />}
      />
    ));
    const regions = this.state.regions.map(region => (
      <ListItem key={region} primaryText={region} leftCheckbox={<Checkbox />} />
    ));
    const nsfw = this.state.nsfws.map(nsfw => (
      <ListItem key={nsfw} primaryText={nsfw} leftCheckbox={<Checkbox />} />
    ));
    const misc = this.state.misc.map(mis => (
      <ListItem key={mis} primaryText={mis} leftCheckbox={<Checkbox />} />
    ));
    return (
      <div>
        <List>
          <Subheader>Languages</Subheader>
          <ListItem
            primaryText="Follow Settings"
            primaryTogglesNestedList={true}
          />
          {languages}
          <ListItem primaryText="Unfollow Settings" />
          {languages}
          <Divider />
          <Subheader>Regions</Subheader>
          <ListItem
            primaryText="Follow Settings"
            primaryTogglesNestedList={true}
          />
          {regions}
          <ListItem primaryText="Unfollow Settings" />
          {regions}
          <Divider />
          <Subheader>NSFW</Subheader>
          <ListItem primaryText="Follow Settings" />
          {nsfw}
          <ListItem primaryText="Unfollow Settings" />
          {nsfw}
          <Divider />
          <Subheader>Miscellaneous</Subheader>
          <ListItem primaryText="Follow Settings" />
          {misc}
          <ListItem primaryText="Unfollow Settings" />
          {misc}
        </List>
      </div>
    );
  }
}

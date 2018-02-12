import React from 'react';
import {List, ListItem} from 'material-ui/List';
import Divider from 'material-ui/Divider';
import Checkbox from 'material-ui/Checkbox';
import Subheader from 'material-ui/Subheader';

const Filter = () => (
  <List>
    <Subheader>Clear all older than</Subheader>
    <ListItem primaryText="One Day" leftCheckbox={<Checkbox />} />
    <ListItem primaryText="One Week" leftCheckbox={<Checkbox />} />
    <Divider />
    <Subheader>Sort by</Subheader>
    <ListItem primaryText="Username" leftCheckbox={<Checkbox />} />
    <ListItem primaryText="Reason" leftCheckbox={<Checkbox />} />
  </List>
);

export default Filter;

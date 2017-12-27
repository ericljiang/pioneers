import React from 'react';
import { withStyles } from 'material-ui/styles';
import Divider from 'material-ui/Divider';
import Drawer from 'material-ui/Drawer';
import List, { ListItem, ListItemText } from 'material-ui/List';
import Typography from 'material-ui/Typography';
import LaunchIcon from 'material-ui-icons/Launch';

const styles = {
  title: {
    margin: "16px",
    width: "264px"
  }
}

function LobbyDrawer(props) {
  return (
    <div>
      <Drawer open={props.open} onClose={props.onClose}>
        <Typography color="secondary" type="title" className={props.classes.title}>
          Bushwhackers of Banan
        </Typography>
        <Divider />
        <List>
          <ListItem
            button
            component="a"
            target="_blank"
            rel="noopener noreferrer"
            href="https://github.com/ericljiang/settlers">
            <ListItemText primary="Read the source on Github" />
            <LaunchIcon />
          </ListItem>
          <ListItem
            button
            component="a"
            target="_blank"
            rel="noopener noreferrer"
            href="https://github.com/ericljiang/settlers/issues/new?labels=user%20reported">
            <ListItemText primary="Report an issue" />
            <LaunchIcon />
          </ListItem>
        </List>
      </Drawer>
    </div>
  );
}

export default withStyles(styles)(LobbyDrawer);

import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import { withStyles } from 'material-ui/styles';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import Tabs, { Tab } from 'material-ui/Tabs';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import MenuIcon from 'material-ui-icons/Menu';
import UserInfo from './UserInfo.js';
import LobbyDrawer from './LobbyDrawer.js'

const styles = {
  button: {
    width: "24px"
  },
  title: {
    marginLeft: "32px",
    flex: 1
  }
}

class LobbyHeader extends Component {
  constructor(props) {
    super(props);
    this.state = { drawerOpen: false };
  }

  openDrawer = () => {
    this.setState({ drawerOpen: true });
  }

  closeDrawer = () => {
    this.setState({ drawerOpen: false });
  }

  render() {
    return (
      <div>
        <AppBar>
          <Toolbar>
            <IconButton
              color="contrast"
              aria-label="Menu"
              onClick={this.openDrawer}
              className={this.props.classes.button}>
              <MenuIcon />
            </IconButton>
            <Typography type="title" color="inherit" className={this.props.classes.title}>
              Lobby
            </Typography>
            <UserInfo displayName={this.props.displayName} onNameChange={this.props.onNameChange} />
          </Toolbar>
          <Tabs value={this.props.view} centered>
            <Tab value="open-games" label="Open Games" component={Link} to="/lobby/open-games" />
            <Tab value="your-games" label="Your Games" component={Link} to="/lobby/your-games" />
          </Tabs>
        </AppBar>
        <LobbyDrawer open={this.state.drawerOpen} onClose={this.closeDrawer} />
      </div>
    );
  }
}

export default withStyles(styles)(LobbyHeader);

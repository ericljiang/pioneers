import React, { Component } from 'react';
import { withStyles } from 'material-ui/styles';
import Button from 'material-ui/Button';
import Menu, { MenuItem } from 'material-ui/Menu';
import ChangeDisplayNameDialog from './ChangeDisplayNameDialog.js';

const styles = {
  button: {
    textTransform: 'none'
  }
}

class UserInfo extends Component {
  constructor(props) {
    super(props);
    this.state = { anchorEl: null, open: false, changeNameDialogOpen: false };
  }

  openMenu = (event) => {
    this.setState({ open: true, anchorEl: event.currentTarget })
  }

  closeMenu = () => {
    this.setState({ open: false })
  }

  openChangeNameDialog = () => {
    this.setState({ changeNameDialogOpen: true });
    this.closeMenu();
  }

  closeChangeNameDialog = () => {
    this.setState({ changeNameDialogOpen: false });
  }

  handleNameChange = (name) => {
    this.props.onNameChange(name);
  }

  render() {
    return (
      <div>
        <Button
          aria-owns={this.state.open ? 'user-info-menu' : null}
          aria-haspopup="true"
          color="contrast"
          onClick={this.openMenu}
          className={this.props.classes.button}>
          {this.props.displayName}
        </Button>
        <Menu
          id="user-info-menu"
          anchorEl={this.state.anchorEl}
          open={this.state.open}
          onClose={this.closeMenu}>
          <MenuItem onClick={this.openChangeNameDialog}>Change display name</MenuItem>
        </Menu>
        <ChangeDisplayNameDialog
          currentName={this.props.displayName}
          open={this.state.changeNameDialogOpen}
          onClose={this.closeChangeNameDialog}
          onSubmit={this.handleNameChange} />
      </div>
    )
  }
}

export default withStyles(styles)(UserInfo);

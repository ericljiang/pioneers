import React, { Component } from 'react';
import Button from 'material-ui/Button';
import TextField from 'material-ui/TextField';
import Dialog, {
  DialogActions,
  DialogContent,
  DialogTitle,
} from 'material-ui/Dialog';

export default class ChangeDisplayNameDialog extends Component {
  constructor(props) {
    super(props);
  }

  handleClose = () => {
    this.props.onClose();
  }

  handleSubmit = () => {
    this.props.onSubmit(this.input.value);
    this.handleClose();
  }

  render() {
    return (
      <Dialog open={this.props.open} onClose={this.handleClose} aria-labelledby="form-dialog-title">
        <DialogTitle id="form-dialog-title">Change display name</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            defaultValue={this.props.currentName}
            margin="dense"
            id="name"
            label="Display Name"
            fullWidth
            inputRef={(input) => this.input = input} />
        </DialogContent>
        <DialogActions>
          <Button onClick={this.handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={this.handleSubmit} color="primary">
            Submit
          </Button>
        </DialogActions>
      </Dialog>
    )
  }
}

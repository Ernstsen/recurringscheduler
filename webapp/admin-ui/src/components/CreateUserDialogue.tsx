import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {User} from "../model/User.ts";
import {useState} from "react";

interface Props {
    open: boolean,
    onClose: () => void,
    addUser: (user: User) => void
}

const validateEmail = (email: string) => {
    return email
        .toLowerCase()
        .match(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );
};

const CreateUserDialogue: React.FC<Props> = ({open, onClose, addUser}) => {

    const [username, setUsername] = useState("")
    const [email, setEmail] = useState("")
    const handleClose = () => {
        onClose()
    };

    const handleCreate = () => {
        if (validateEmail(email) && email.length > 0) {
            addUser(new User(
                null,
                username,
                email
            ))
            onClose()
        }
    };

    return (
        <React.Fragment>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Create new User</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Enter information of the user you want to create.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="name"
                        label="First name"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            setUsername(event.target.value);
                        }}
                    />
                    <TextField
                        margin="dense"
                        id="name"
                        label="Email Address"
                        type="email"
                        fullWidth
                        variant="standard"
                        error={!validateEmail(email) && email.length > 0}
                        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            setEmail(event.target.value);
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button color="error" onClick={handleClose}>Cancel</Button>
                    <Button color="primary" onClick={handleCreate} disabled={!validateEmail(email)}>Create</Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
}

export default CreateUserDialogue;

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

interface CreateUserProps {
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

export const EditUserDialogue: React.FC<CreateUserProps> = ({open, onClose, addUser}) => {
    return (
        <React.Fragment>
            <GenericUserDialogue
                open={open}
                onClose={onClose}
                commitChanges={addUser}
                commitButtonText="Create"
                title="Create new User">
            </GenericUserDialogue>
        </React.Fragment>
    );
}

interface ModifyUserProps {
    open: boolean,
    onClose: () => void,
    saveChanges: (user: User) => void,
    existingUser: User
}

export const ModifyUserDialogue: React.FC<ModifyUserProps> = ({open, onClose, saveChanges, existingUser}) => {
    return (
        <React.Fragment>
            <GenericUserDialogue
                open={open}
                onClose={onClose}
                commitChanges={saveChanges}
                commitButtonText="Update"
                title="Update User"
                existingUser={existingUser}
            >
            </GenericUserDialogue>
        </React.Fragment>
    );

}

interface GenericProps {
    open: boolean,
    onClose: () => void,
    commitChanges: (user: User) => void,
    commitButtonText: string,
    title: string,
    existingUser?: User
}

export const GenericUserDialogue: React.FC<GenericProps> = (
    {
        open,
        onClose,
        commitChanges,
        commitButtonText,
        title,
        existingUser
    }) => {

    const [username, setUsername] = useState(existingUser?.name || "")
    const [email, setEmail] = useState(existingUser?.email || "")
    const handleClose = () => {
        onClose()
    };

    const handleCreate = () => {
        if (validateEmail(email) && email.length > 0) {
            commitChanges(new User(
                existingUser?.id || null,
                username,
                email
            ))
            onClose()
        }
    };

    return (
        <React.Fragment>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>{title}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        User information
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
                        defaultValue={existingUser?.name}
                    />
                    <TextField
                        margin="dense"
                        id="email"
                        label="Email Address"
                        type="email"
                        fullWidth
                        variant="standard"
                        error={!validateEmail(email) && email.length > 0}
                        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            setEmail(event.target.value);
                        }}
                        defaultValue={existingUser?.email}
                    />
                </DialogContent>
                <DialogActions>
                    <Button color="error" onClick={handleClose}>Cancel</Button>
                    <Button color="primary" onClick={handleCreate}
                            disabled={!validateEmail(email)}>{commitButtonText}</Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
}

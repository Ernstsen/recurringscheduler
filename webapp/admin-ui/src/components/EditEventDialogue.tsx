import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {Event} from "../model/Event.ts";
import {useState} from "react";
import InputLabel from "@mui/material/InputLabel";

interface CreateEventProps {
    open: boolean,
    onClose: () => void,
    addEvent: (event: Event) => void
}

export const EditEventDialogue: React.FC<CreateEventProps> = ({open, onClose, addEvent}) => {
    return (
        <React.Fragment>
            <GenericEventDialogue
                open={open}
                onClose={onClose}
                commitChanges={addEvent}
                commitButtonText="Create"
                title="Create new Event Type">
            </GenericEventDialogue>
        </React.Fragment>
    );
}

interface ModifyEventProps {
    open: boolean,
    onClose: () => void,
    saveChanges: (event: Event) => void,
    existingEvent: Event
}

export const ModifyEventDialogue: React.FC<ModifyEventProps> =
    ({
         open,
         onClose,
         saveChanges,
         existingEvent
     }) => {
        return (
            <React.Fragment>
                <GenericEventDialogue
                    open={open}
                    onClose={onClose}
                    commitChanges={saveChanges}
                    commitButtonText="Update"
                    title="Update Event"
                    existingEvent={existingEvent}
                >
                </GenericEventDialogue>
            </React.Fragment>
        );

    }

interface GenericProps {
    open: boolean,
    onClose: () => void,
    commitChanges: (event: Event) => void,
    commitButtonText: string,
    title: string,
    existingEvent?: Event
}

export const GenericEventDialogue: React.FC<GenericProps> = (
    {
        open,
        onClose,
        commitChanges,
        commitButtonText,
        title,
        existingEvent
    }) => {
    const [eventname, setEventname] = useState(existingEvent?.name || "")
    const handleClose = () => {
        onClose()
    };

    const validateInput = (): boolean => {
        return eventname.length > 0;
    }

    const handleCreate = () => {
        if (validateInput()) {
            commitChanges(new Event(
                existingEvent?.id || null,
                eventname,
                [],
                null
            ))
            onClose()
        }
    };

    return (
        <React.Fragment>
            <Dialog open={open} onClose={handleClose} fullWidth>
                <DialogTitle>{title}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Event information
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="eventname"
                        label="Event Type name"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            setEventname(event.target.value);
                        }}
                        defaultValue={existingEvent?.name}
                    />
                    <InputLabel id="recurrenceConfigurationSelector">Recurrence Configuration</InputLabel>
                </DialogContent>
                <DialogActions>
                    <Button color="error" onClick={handleClose}>Cancel</Button>
                    <Button color="primary" onClick={handleCreate}
                            disabled={!validateInput()}>{commitButtonText}</Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
}

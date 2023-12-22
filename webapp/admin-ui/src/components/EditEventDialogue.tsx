import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {Event} from "../model/Event.ts";
import InputLabel from "@mui/material/InputLabel";
import Select, {SelectChangeEvent} from "@mui/material/Select";
import {MenuItem} from "@mui/material";
import useEventTypeClient from "../client/EventTypeClient.ts";
import styles from "./GenericDialogue.module.css"

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
                title="Create new Event">
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
    const [eventTypeId, setEventTypeId] = useState(existingEvent?.type.id || "")
    const [chosenTime, setChosenTime] = useState(existingEvent?.chosenTime?.toString || "")
    const [eventTypes] = useEventTypeClient();
    const handleClose = () => {
        onClose()
    };

    const validateInput = (): boolean => {
        let chosenEventType = eventTypes.find((eventType) => eventType.id === eventTypeId);
        return chosenEventType !== undefined && eventname.length > 0;
    }

    const handleCreate = () => {
        let chosenEventType = eventTypes.find((eventType) => eventType.id === eventTypeId);

        if (chosenEventType && validateInput()) {
            commitChanges(new Event(
                existingEvent?.id || null,
                eventname,
                chosenEventType,
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
                    <DialogContentText className={styles.DialogContentText}>
                        General information about the event
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
                    <Select
                        margin="dense"
                        labelId="recurrenceConfigurationSelector"
                        name="Recurring Configuration"
                        fullWidth
                        variant="standard"
                        onChange={(event: SelectChangeEvent) => {
                            setEventTypeId(event.target.value)
                        }}
                        defaultValue={eventTypes.find((eventType) => eventType.id === eventTypeId)?.id}
                        value={eventTypeId}
                    >
                        {
                            eventTypes.map((eventType) =>
                                <MenuItem value={eventType.id} key={eventType.id}>
                                    {eventType.name}
                                </MenuItem>
                            )
                        }
                    </Select>
                </DialogContent>
                <DialogContent>
                    <DialogContentText className={styles.DialogContentText}>
                        Available Dates
                    </DialogContentText>
                    <InputLabel id="chosenDate">Chosen Date</InputLabel>
                    <Select
                        margin="dense"
                        labelId="recurrenceConfigurationSelector"
                        name="Recurring Configuration"
                        fullWidth
                        variant="standard"
                        onChange={(event: SelectChangeEvent) => {
                            setChosenTime(event.target.value)
                        }}
                        value={chosenTime}
                    >
                        {
                            ["1/2/2023", "2/2/2023"].map((possibleDate) =>
                                <MenuItem value={possibleDate}>
                                    {possibleDate}
                                </MenuItem>
                            )
                        }
                    </Select>
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

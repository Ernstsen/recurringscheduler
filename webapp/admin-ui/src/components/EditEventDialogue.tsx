import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import LinearProgress from '@mui/material/LinearProgress';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {Event} from "../model/Event.ts";
import InputLabel from "@mui/material/InputLabel";
import Select, {SelectChangeEvent} from "@mui/material/Select";
import {Alert, MenuItem} from "@mui/material";
import useEventTypeClient from "../client/EventTypeClient.ts";
import styles from "./GenericDialogue.module.css"
import PossibleTimesView from "./datepicking/PossibleTimesView.tsx";

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
    const [chosenTime, setChosenTime] = useState(existingEvent?.chosenTime)
    const [possibleTimes, setPossibleTimes] = useState(existingEvent?.possibleTimes || [])
    const [eventTypes, , , , eventTypeError, eventTypeLoading] = useEventTypeClient();

    if (eventTypeError) {
        return <Alert severity="error">Failed to read EventTypes from server</Alert>
    }
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
                possibleTimes,
                chosenTime || null
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
                    {eventTypeLoading ? <LinearProgress/> :  <Select
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
                    </Select>}
                </DialogContent>
                <DialogContent>
                    <DialogContentText className={styles.DialogContentText}>
                        Available Dates
                    </DialogContentText>
                    <InputLabel id="chosenDateLabel">Chosen Date</InputLabel>
                    <Select
                        margin="dense"
                        labelId="chosenDateLabel"
                        name="Chosen Date"
                        fullWidth
                        variant="standard"
                        onChange={(event: SelectChangeEvent) => {
                            setChosenTime(new Date(event.target.value))
                        }}
                        defaultValue={existingEvent?.chosenTime?.toISOString()}
                    >
                        {
                            possibleTimes.map((possibleDate) =>
                                <MenuItem value={possibleDate.toISOString()} key={possibleDate.toISOString()}>
                                    {possibleDate.toDateString()}
                                </MenuItem>
                            )
                        }
                    </Select>
                    <PossibleTimesView
                        possibleTimes={possibleTimes}
                        onPossibleTimeRemoval={(possibleTime) => setPossibleTimes(possibleTimes.filter((time) => time !== possibleTime))}
                        onPossibleTimeAddition={(possibleTime) => setPossibleTimes([...possibleTimes, possibleTime].sort((a, b) => a.getTime() - b.getTime()))}
                    />
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

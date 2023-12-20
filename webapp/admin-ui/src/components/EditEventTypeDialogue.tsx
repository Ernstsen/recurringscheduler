import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {EventType} from "../model/EventType.ts";
import {useState} from "react";
import useRecurrenceConfigurationClient from "../client/UseRecurrenceConfigurationClient.ts";
import Select, {SelectChangeEvent} from "@mui/material/Select";
import {MenuItem} from "@mui/material";
import InputLabel from "@mui/material/InputLabel";

interface CreateEventTypeProps {
    open: boolean,
    onClose: () => void,
    addEventType: (eventType: EventType) => void
}

export const EditEventTypeDialogue: React.FC<CreateEventTypeProps> = ({open, onClose, addEventType}) => {
    return (
        <React.Fragment>
            <GenericEventTypeDialogue
                open={open}
                onClose={onClose}
                commitChanges={addEventType}
                commitButtonText="Create"
                title="Create new Event Type">
            </GenericEventTypeDialogue>
        </React.Fragment>
    );
}

interface ModifyEventTypeProps {
    open: boolean,
    onClose: () => void,
    saveChanges: (eventType: EventType) => void,
    existingEventType: EventType
}

export const ModifyEventTypeDialogue: React.FC<ModifyEventTypeProps> =
    ({
         open,
         onClose,
         saveChanges,
         existingEventType
     }) => {
        return (
            <React.Fragment>
                <GenericEventTypeDialogue
                    open={open}
                    onClose={onClose}
                    commitChanges={saveChanges}
                    commitButtonText="Update"
                    title="Update EventType"
                    existingEventType={existingEventType}
                >
                </GenericEventTypeDialogue>
            </React.Fragment>
        );

    }

interface GenericProps {
    open: boolean,
    onClose: () => void,
    commitChanges: (eventType: EventType) => void,
    commitButtonText: string,
    title: string,
    existingEventType?: EventType
}

export const GenericEventTypeDialogue: React.FC<GenericProps> = (
    {
        open,
        onClose,
        commitChanges,
        commitButtonText,
        title,
        existingEventType
    }) => {
    const [recurrenceConfigurations] = useRecurrenceConfigurationClient();
    const [eventTypename, setEventTypename] = useState(existingEventType?.name || "")
    const [recurrenceConfigurationId, setRecurrenceConfigurationId] = useState(existingEventType?.recurrenceConfiguration.id || "")
    const handleClose = () => {
        onClose()
    };

    const validateInput = (): boolean => {
        let chosenConfig = recurrenceConfigurations.find((recurrenceConfiguration) => recurrenceConfiguration.id === recurrenceConfigurationId);
        return eventTypename.length > 0 && chosenConfig !== undefined;
    }

    const handleCreate = () => {
        let chosenRecurrenceConfig = recurrenceConfigurations.find((recurrenceConfiguration) => recurrenceConfiguration.id === recurrenceConfigurationId);

        if (chosenRecurrenceConfig && validateInput()) {
            commitChanges(new EventType(
                existingEventType?.id || null,
                eventTypename,
                chosenRecurrenceConfig
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
                        EventType information
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="eventTypename"
                        label="Event Type name"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            setEventTypename(event.target.value);
                        }}
                        defaultValue={existingEventType?.name}
                    />
                    <InputLabel id="recurrenceConfigurationSelector">Recurrence Configuration</InputLabel>
                    <Select
                        margin="dense"
                        labelId="recurrenceConfigurationSelector"
                        name="Recurring Configuration"
                        fullWidth
                        variant="standard"
                        onChange={(event: SelectChangeEvent) => {
                            setRecurrenceConfigurationId(event.target.value)
                        }}
                        defaultValue={recurrenceConfigurations.find((recurrenceConfiguration) => recurrenceConfiguration.id === recurrenceConfigurationId)?.id}
                        value={recurrenceConfigurationId}
                    >
                        {
                            recurrenceConfigurations.map((recurrenceConfiguration) =>
                                <MenuItem value={recurrenceConfiguration.id} key={recurrenceConfiguration.id}>
                                    {recurrenceConfiguration.name}
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

import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import Select, {SelectChangeEvent} from '@mui/material/Select';
import InputLabel from '@mui/material/InputLabel';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {RecurrenceConfiguration} from "../model/RecurrenceConfiguration.ts";
import {useState} from "react";
import {MenuItem} from "@mui/material";

interface CreateRecurrenceConfigurationProps {
    open: boolean,
    onClose: () => void,
    addRecurrenceConfiguration: (recurrenceConfiguration: RecurrenceConfiguration) => void
}

export const CreateRecurrenceConfigurationDialogue: React.FC<CreateRecurrenceConfigurationProps> = (
    {
        open,
        onClose,
        addRecurrenceConfiguration
    }) => {
    return (
        <React.Fragment>
            <GeneriRecurrenceConfigurationDialogue
                open={open}
                onClose={onClose}
                commitChanges={addRecurrenceConfiguration}
                commitButtonText="Create"
                title="Create new Recurrence Configuration">
            </GeneriRecurrenceConfigurationDialogue>
        </React.Fragment>
    );
}

interface ModifyRecurrenceConfigurationProps {
    open: boolean,
    onClose: () => void,
    saveChanges: (recurrenceConfiguration: RecurrenceConfiguration) => void,
    existingRecurrenceConfiguration?: RecurrenceConfiguration
}

export const ModifyRecurrenceConfigurationDialogue: React.FC<ModifyRecurrenceConfigurationProps> =
    ({
         open,
         onClose,
         saveChanges,
         existingRecurrenceConfiguration
     }) => {
        return (
            <React.Fragment>
                <GeneriRecurrenceConfigurationDialogue
                    open={open}
                    onClose={onClose}
                    commitChanges={saveChanges}
                    commitButtonText="Update"
                    title="Update Recurrence Configuration"
                    existingRecurrenceConfiguration={existingRecurrenceConfiguration}
                >
                </GeneriRecurrenceConfigurationDialogue>
            </React.Fragment>
        );

    }

interface GenericProps {
    open: boolean,
    onClose: () => void,
    commitChanges: (recurrenceConfiguration: RecurrenceConfiguration) => void,
    commitButtonText: string,
    title: string,
    existingRecurrenceConfiguration?: RecurrenceConfiguration
}

export const GeneriRecurrenceConfigurationDialogue: React.FC<GenericProps> = (
    {
        open,
        onClose,
        commitChanges,
        commitButtonText,
        title,
        existingRecurrenceConfiguration
    }) => {

    const [name, setName] = useState(existingRecurrenceConfiguration?.name || "")
    const [timeUnit, setTimeUnit] = useState(existingRecurrenceConfiguration?.timeUnit || "")
    const [occurrencesPerTimePeriod, setOccurrencesPerTimePeriod] = useState(existingRecurrenceConfiguration?.occurrencesPerTimePeriod || 1)
    const handleClose = () => {
        onClose()
    };

    const handleCreate = () => {
        commitChanges(new RecurrenceConfiguration(
            name,
            timeUnit,
            occurrencesPerTimePeriod,
            existingRecurrenceConfiguration?.id,
        ))
        onClose()
    }

    const allInputsValid = () => {
        return name.length > 0 && timeUnit.length > 0 && occurrencesPerTimePeriod > 0
    }

    return (
        <React.Fragment>
            <Dialog open={open} onClose={handleClose} fullWidth>
                <DialogTitle>{title}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Recurrence Configuration Information
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Name"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            setName(event.target.value);
                        }}
                        defaultValue={existingRecurrenceConfiguration?.name}
                    />
                    <InputLabel id="timeUnitSelector">Time Unit</InputLabel>
                    <Select
                        margin="dense"
                        labelId="timeUnitSelector"
                        name="Time Unit"
                        fullWidth
                        variant="standard"
                        onChange={(event: SelectChangeEvent) => {
                            console.log(event.target.value)
                            setTimeUnit(event.target.value);
                        }}
                        defaultValue={existingRecurrenceConfiguration ? existingRecurrenceConfiguration.timeUnit : "Week"}
                        value={timeUnit}
                    >
                        <MenuItem value="WEEK">Week</MenuItem>
                        <MenuItem value="MONTH">Month</MenuItem>
                        <MenuItem value="Year">year</MenuItem>
                    </Select>
                    <TextField
                        margin="dense"
                        label="occurrencesPerTimePeriod"
                        type="number"
                        fullWidth
                        variant="standard"
                        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            setOccurrencesPerTimePeriod(event.target.valueAsNumber);
                        }}
                        defaultValue={existingRecurrenceConfiguration ? existingRecurrenceConfiguration.occurrencesPerTimePeriod : 1}
                    />
                </DialogContent>
                <DialogActions>
                    <Button color="error" onClick={handleClose}>Cancel</Button>
                    <Button color="primary" onClick={handleCreate}
                            disabled={!allInputsValid()}>{commitButtonText}</Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
}

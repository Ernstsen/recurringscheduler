import * as React from 'react';
import {useState} from 'react';
import {
    Card,
    CardActions,
    CardContent,
    Dialog,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Grid,
    IconButton,
    Typography
} from "@mui/material";
import styles from "./PossibleTimesView.module.css"
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import {DatePicker} from '@mui/x-date-pickers/DatePicker';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import dayjs from "dayjs";

interface PossibleTimesViewprops {
    possibleTimes: Date[],
    onPossibleTimeRemoval: (possibleTime: Date) => void,
    onPossibleTimeAddition: (possibleTime: Date) => void
}

const weekdayToString = (weekDay: number): string => {
    return ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"][weekDay];
}

interface NewTimeDialogProps {
    open: boolean,
    onClose: () => void,
    onPossibleTimeAddition: (possibleTime: Date) => void;
}

const NewTimeDialog: React.FC<NewTimeDialogProps> =
    ({
         open,
         onClose,
         onPossibleTimeAddition
     }) => {
        const [chosenDate, setChosenDate] = useState(dayjs())
        return (
            <Dialog open={open} onClose={onClose} fullWidth>
                <DialogTitle>Add Date</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Choose date to add as possibility
                    </DialogContentText>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            label="Date to be added"
                            value={chosenDate}
                            onAccept={chosen => chosen && setChosenDate(chosen)}
                        />
                    </LocalizationProvider>
                </DialogContent>
                <DialogActions>
                    <Button color="error" onClick={onClose}>Cancel</Button>
                    <Button color="primary" onClick={() => {
                        onPossibleTimeAddition(chosenDate.toDate());
                        onClose();
                    }}>
                        Add
                    </Button>
                </DialogActions>
            </Dialog>
        )
    }


interface PossibleTimeCardProps {
    onPossibleTimeAddition: (possibleTime: Date) => void;
}

const PossibleTimeAddCard: React.FC<PossibleTimeCardProps> = ({onPossibleTimeAddition}) => {
    const [timeDialogOpen, setTimeDialogOpen] = useState(false)

    return (
        <Grid item xs={4}>
            <Card>
                <NewTimeDialog
                    onPossibleTimeAddition={onPossibleTimeAddition}
                    open={timeDialogOpen}
                    onClose={() => setTimeDialogOpen(false)}
                />
                <CardActions disableSpacing>
                    <CardContent>
                        <Typography gutterBottom variant="body1" component="div">
                            New time
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Adds option
                        </Typography>
                    </CardContent>
                    <IconButton aria-label="Add new Date"
                                onClick={() => setTimeDialogOpen(true)}
                    >
                        <AddIcon/>
                    </IconButton>
                </CardActions>
            </Card>
        </Grid>
    );
}

const PossibleTimesView: React.FC<PossibleTimesViewprops> =
    ({
         possibleTimes,
         onPossibleTimeRemoval,
         onPossibleTimeAddition
     }) => {
        return (
            <Grid container spacing={3} className={styles.OuterView}>
                <PossibleTimeAddCard onPossibleTimeAddition={onPossibleTimeAddition}/>
                {
                    possibleTimes
                        .map((possibleTime) =>
                            <Grid item xs={4}>
                                <Card>
                                    <CardActions disableSpacing>
                                        <CardContent>
                                            <Typography gutterBottom variant="body1" component="div">
                                                {possibleTime.toLocaleString("da").split(" ")[0]}
                                            </Typography>
                                            <Typography variant="body2" color="text.secondary">
                                                {weekdayToString(possibleTime.getDay())}
                                            </Typography>
                                        </CardContent>
                                        <IconButton aria-label="Remove from available dates"
                                                    onClick={() => onPossibleTimeRemoval(possibleTime)}>
                                            <DeleteIcon/>
                                        </IconButton>
                                    </CardActions>
                                </Card>
                            </Grid>
                        )
                }
            </Grid>
        )
    }

export default PossibleTimesView;
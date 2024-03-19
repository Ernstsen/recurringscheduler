import React, {useState} from 'react'
import {Box, Button, Grid, IconButton, Paper} from "@mui/material";
import styles from "./CollectView.module.css";
import {Check, Close} from "@mui/icons-material";
import useUserResponseClient from "../client/UserResponseClient.ts";
import {UserResponse} from "../model/UserResponse.ts";

interface CollectViewProps {
    collectId: string
}

export const LoadingView = () => {
    return (
        <React.Fragment>
            <h1>Collector</h1>
            <p>
                Loading...
            </p>
        </React.Fragment>
    )
}

export const ErrorView = () => {
    return (
        <React.Fragment>
            <h1>Collector</h1>
            <p>
                Error...
            </p>
        </React.Fragment>
    )
}

class PossibleDate {
    date: Date;
    available: boolean;

    constructor(date: Date, available: boolean) {
        this.date = date;
        this.available = available;
    }

}


function CollectViewContent({userResponse, updateResponse}: {
    userResponse: UserResponse,
    updateResponse: (userResponse: UserResponse) => void
}) {
    const [chosenDates, setChosenDates] = useState(userResponse.chosenDates || [])

    let possibleTimes = userResponse.event.possibleTimes;

    let currentDatesState = possibleTimes.map(dateOption => new PossibleDate(dateOption, chosenDates.map(pd => pd.toLocaleDateString()).includes(dateOption.toLocaleDateString())));

    return (
        <Paper className={styles.Paper} elevation={10} variant={"elevation"}>
            <h1>Choose dates available</h1>
            <h2>{userResponse.event.name}</h2>

            <Box sx={{flexGrow: 1}}>
                <Grid container spacing={{xs: 2, md: 3}} columns={{xs: 4, sm: 8, md: 12}}>
                    {currentDatesState.map(dateOption => (
                        <Grid item xs={2} sm={4} md={4} key={dateOption.date.toLocaleDateString()}>
                            <Box
                                display="flex"
                                p={1.5}
                                gap={2}
                                className={styles.Card}
                                borderRadius={4}
                            >
                                <Box sx={{flex: "auto"}}>
                                    <h3>{["Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"][dateOption.date.getDay()]}. {dateOption.date.toLocaleDateString()}</h3>
                                    <p>{dateOption.available ? "I'm Available" : "I'm unavailable"}</p>
                                </Box>
                                <Box ml={1}>
                                    <IconButton size="small"
                                                onClick={() => {
                                                    setChosenDates(dateOption.available ? chosenDates.filter(date => date.toLocaleDateString() !== dateOption.date.toLocaleDateString()) : [...chosenDates, dateOption.date])
                                                }}
                                    >
                                        {dateOption.available ? <Close/> : <Check/>}
                                    </IconButton>
                                </Box>
                            </Box>
                        </Grid>
                    ))}
                </Grid>
            </Box>

            <Button title={"Submit"} onClick={() => {
                userResponse.chosenDates = chosenDates;
                updateResponse(userResponse)
            }}>Submit</Button>
        </Paper>
    )
}

export const CollectView: React.FC<CollectViewProps> = ({collectId}) => {
    const [
        userResponse,
        updateResponse,
        error,
        loading,
    ] = useUserResponseClient(collectId)

    if (loading) {
        return <LoadingView/>
    }

    if (error || !userResponse) {
        return <ErrorView/>
    }

    return (
        <React.Fragment>
            <CollectViewContent
                userResponse={userResponse}
                updateResponse={updateResponse}/>

        </React.Fragment>
    )
}

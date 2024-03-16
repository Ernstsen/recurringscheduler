import React from 'react'
import {Box, Button, Grid, IconButton, Paper} from "@mui/material";
import styles from "./CollectView.module.css";
import {Add} from "@mui/icons-material";
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

function CollectViewContent({userResponse, updateResponse}: {
    userResponse: UserResponse,
    updateResponse: (userResponse: UserResponse) => void
}) {

    let possibleTimes = userResponse.event.possibleTimes;

    let chosenDates = userResponse.chosenTimes || [];

    return (
        <Paper className={styles.Paper} elevation={10} variant={"elevation"}>
            <h1>Choose dates available</h1>
            <h2>{userResponse.event.name}</h2>

            <Box sx={{flexGrow: 1}}>
                <Grid container spacing={{xs: 2, md: 3}} columns={{xs: 4, sm: 8, md: 12}}>
                    {possibleTimes.map(dateOption => {
                        let isChosen = chosenDates.filter(d => d.toLocaleDateString() === dateOption.toLocaleDateString()).length > 0;
                        return (
                            <Grid item xs={2} sm={4} md={4} key={dateOption.toLocaleDateString()}>
                                <Box
                                    display="flex"
                                    p={1.5}
                                    gap={2}
                                    bgcolor={"#f5f5f5"}
                                    borderRadius={4}
                                    sx={{alignItems: "center"}}
                                >
                                    <Box sx={{flex: "auto"}}>
                                        <h3>{["Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"][dateOption.getDay()]}. {dateOption.toLocaleDateString()}</h3>
                                        <p>{isChosen ? "Available" : "Unavailable"}</p>
                                    </Box>
                                    <Box ml={1}>
                                        <IconButton size="small"
                                                    action={() => console.log("Toggle for date: " + dateOption.toLocaleDateString())}
                                        >
                                            <Add/>
                                        </IconButton>
                                    </Box>
                                </Box>
                            </Grid>
                        )
                    })}
                </Grid>
            </Box>

            <Button title={"Submit"} action={() => updateResponse(userResponse)}>Submit</Button>
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

    if (error) {
        return <ErrorView/>
    }

    return (
        <React.Fragment>
            {((!loading && !error) && userResponse) ?
                (<CollectViewContent
                    userResponse={userResponse}
                    updateResponse={updateResponse}/>) :
                (<p>Error</p>)
            }
        </React.Fragment>
    )
}

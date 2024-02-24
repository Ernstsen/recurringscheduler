import React from 'react'
import {Paper} from "@mui/material";
import styles from "./CollectView.module.css";
import useUserResponseClient from "../client/UserResponseClient.ts";
import {UserResponse} from "../model/UserResponse.ts";
import {Event} from "../model/Event.ts";

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

function CollectViewContent(props: {
    event: Event,
    userResponse: UserResponse,
    updateResponse: (userResponse: UserResponse) => void
}) {
    return (
        <Paper className={styles.Paper} elevation={10} variant={"elevation"}>
            <h1>Collector</h1>
            <p>
                Collecting data for {props.userResponse.id}
            </p>
        </Paper>
    )
}

export const CollectView: React.FC<CollectViewProps> = ({collectId}) => {
    const [
        userResponse,
        event,
        updateResponse,
        error,
        loading,
    ] =  useUserResponseClient(collectId)


    return (
        <React.Fragment>
            {loading && <LoadingView/>}
            {error && <ErrorView/>}

            {(!loading && !error) &&
                <CollectViewContent
                    userResponse={userResponse}
                    event={event}
                    updateResponse={updateResponse}/>
            }
        </React.Fragment>
    )
}

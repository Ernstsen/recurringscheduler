import React from 'react'
import {Paper} from "@mui/material";
import styles from "./CollectView.module.css";
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

function CollectViewContent(props: {
    userResponse: UserResponse,
    updateResponse: (userResponse: UserResponse) => void
}) {
    return (
        <Paper className={styles.Paper} elevation={10} variant={"elevation"}>
            <h1>Choose dates available for {props.userResponse.event.name}</h1>
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

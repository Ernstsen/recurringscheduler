import React, {useState} from 'react'
import {Paper} from "@mui/material";
import styles from "./App.module.css";
import {useParams} from "react-router-dom";

function App() {
    let {collectId} = useParams();

    return (
        <React.Fragment>
            <Paper className={styles.Paper} elevation={10} variant={"elevation"}>
                <h1>Collector</h1>
                <p>
                    Collecting data for {collectId}
                </p>
            </Paper>
        </React.Fragment>
    )
}

export default App

import React from 'react';
import {Button, Grid, Paper, TextField, Typography} from '@mui/material';
import {AuthControl, useAuth} from "./UseAuthentication.tsx";
import useStyles from './LoginPage.module.css';
import {User} from "../model/User.ts";

const LoginPage: React.FC = () => {
    const authMemory: AuthControl = useAuth();

    return (
        <Grid container className={useStyles.container} justifyContent="center" alignItems="center">
            <Grid item xs={12} sm={8} md={6}>
                <Paper className={useStyles.paper} elevation={3}>
                    <Typography variant="h4" gutterBottom>
                        Login
                    </Typography>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        fullWidth
                        id="username"
                        label="Username"
                        name="username"
                        autoComplete="username"
                        autoFocus
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={useStyles.submitButton}
                        onClick={() => {
                            console.log("Logging in!")
                            authMemory.login(new User(null, "test", "test"))
                        }}
                    >
                        Login
                    </Button>
                </Paper>
            </Grid>
        </Grid>
    );
};

export default LoginPage;

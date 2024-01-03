import React, {useState} from 'react';
import {Button, Grid, Paper, TextField, Typography} from '@mui/material';
import {AuthControl, useAuth} from "./UseAuthentication.tsx";
import useStyles from './LoginPage.module.css';
import {useNavigate} from "react-router-dom";

const LoginPage: React.FC = () => {
    const authMemory: AuthControl = useAuth();
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    let navigate = useNavigate();

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
                        id="email"
                        label="Email Address"
                        type="email"
                        name="email"
                        autoComplete="email"
                        autoFocus
                        onChange={(event) => setEmail(event.target.value)}
                        value={email}
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
                        onChange={(event) => setPassword(event.target.value)}
                        value={password}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={useStyles.submitButton}
                        onClick={() => {
                            console.log("Logging in!")
                            authMemory.login(email, password).then((authResponse) => {
                                console.log("Logged in!", authResponse)
                                navigate("/");
                            })
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

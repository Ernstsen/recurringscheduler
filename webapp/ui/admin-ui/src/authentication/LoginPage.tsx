import React, {useState} from 'react';
import {Alert, Button, Grid, Paper, TextField, Typography} from '@mui/material';
import {AuthControl, useAuth} from "./UseAuthentication.tsx";
import styles from './LoginPage.module.css';
import {useNavigate} from "react-router-dom";
import Box from "@mui/material/Box";
import imgUrl from './logo_transparent.png';

const LoginPage: React.FC = () => {
    const authMemory: AuthControl = useAuth();
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")
    let navigate = useNavigate();

    return (
        <Grid container className={styles.container} justifyContent="center" alignItems="center">
            <Grid item xs={12} sm={8} md={6}>
                <Paper className={styles.paper} elevation={3} sx={{padding: "2vw"}}>
                    <Box sx={{width: 200, height: 200, margin: "auto"}}
                         component={"img"}
                         src={imgUrl}
                    />
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
                        className={styles.submitButton}
                        onClick={() => {
                            authMemory.login(email, password)
                                .then(() => {
                                    navigate("/");
                                }, (error) => {
                                    setError(error)
                                })
                        }}
                    >
                        Login
                    </Button>
                    {error && <Alert severity="error" sx={{margin: "1lh"}}>{error}</Alert>}
                </Paper>
            </Grid>
        </Grid>
    );
};

export default LoginPage;

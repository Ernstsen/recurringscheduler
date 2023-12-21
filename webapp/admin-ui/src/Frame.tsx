import {Drawer, IconButton, List, ListItem, ListItemButton, ListItemIcon, ListItemText, styled} from "@mui/material";
import {Outlet, useNavigate} from "react-router-dom";
import Box from '@mui/material/Box';
import AppBar from '@mui/material/AppBar';
import CssBaseline from '@mui/material/CssBaseline';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import HomeIcon from '@mui/icons-material/Home';
import MenuIcon from '@mui/icons-material/Menu';
import GroupIcon from '@mui/icons-material/Group';
import LoopIcon from '@mui/icons-material/Loop';
import EventRepeatIcon from '@mui/icons-material/EventRepeat';
import EventIcon from '@mui/icons-material/Event';
import React from "react";


const pages = [
    {"name": "Frontpage", "path": "/", "icon": <HomeIcon/>},
    {"name": "Users", "path": "/users", "icon": <GroupIcon/>},
    {"name": "Recurrence Configs.", "path": "/recurrenceConfigurations", "icon": <LoopIcon/>},
    {"name": "Event Types", "path": "/eventTypes", "icon": <EventRepeatIcon/>},
    {"name": "Events", "path": "/events", "icon": <EventIcon/>},
];

const drawerWidth = 240;

const Main = styled('main', {shouldForwardProp: (prop) => prop !== 'open'})<{
    open?: boolean;
}>(({theme, open}) => ({
    flexGrow: 1,
    padding: theme.spacing(3),
    transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    marginLeft: `-${drawerWidth}px`,
    ...(open && {
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
        marginLeft: 0,
    }),
}));

function Frame() {
    let navigate = useNavigate();
    const [open, setOpen] = React.useState(true);

    return (
        <Box sx={{display: 'flex'}}>
            <CssBaseline/>
            <AppBar position="fixed" sx={{zIndex: (theme) => theme.zIndex.drawer + 1}}>
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        edge="start"
                        onClick={() => setOpen(!open)}
                    >
                        <MenuIcon/>
                    </IconButton>
                    <Typography variant="h6" noWrap component="div">
                        Recurring Scheduler Administration
                    </Typography>
                </Toolbar>
            </AppBar>
            <Drawer
                variant="persistent"
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    [`& .MuiDrawer-paper`]: {width: drawerWidth, boxSizing: 'border-box'},
                }}
                open={open}
            >
                <Toolbar/>
                <Box sx={{overflow: 'auto'}}>
                    <List>
                        {pages.map((destination) => (
                            <ListItem key={destination.name} disablePadding>
                                <ListItemButton onClick={() => navigate(destination.path)}>
                                    <ListItemIcon>
                                        {destination.icon}
                                    </ListItemIcon>
                                    <ListItemText primary={destination.name}/>
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Box>
            </Drawer>
            <Main open={open}>
                <Toolbar/>
                <Outlet/>
            </Main>
        </Box>
    );
}

export default Frame

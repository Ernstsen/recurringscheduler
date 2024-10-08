import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';
import {useState} from "react";
import {DataGrid, GridActionsCellItem, GridColDef, GridValueGetterParams} from '@mui/x-data-grid';
import {CreateEventTypeDialogue, ModifyEventTypeDialogue} from "../components/CreateEventTypeDialogue.tsx";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import useEventTypeClient from "../client/EventTypeClient.ts";
import {useNavigate, useParams} from "react-router-dom";
import {EventType} from "../model/EventType.ts";
import {Alert, Backdrop, CircularProgress, LinearProgress} from "@mui/material";
import {useCreateEventFromEventType} from "../client/EventTypeClient.ts";

function EventTypesPage() {
    const [eventTypes, addEventType, updateEventType, deleteEventType, eventTypeError, eventTypeLoading] = useEventTypeClient()
    const [createEventTypeOpen, setCreateEventTypeOpen] = useState(false)
    const [actionLoading, setActionLoading] = useState(false)
    const {eventTypeId} = useParams()
    const navigate = useNavigate();
    const createEventFromEventType = useCreateEventFromEventType()

    if (eventTypeError) {
        return <Alert severity="error">Failed to read EventTypes from server</Alert>
    }

    const editingEventType: EventType | null = eventTypeId ? eventTypes.filter(eventType => eventType.id === eventTypeId)[0] : null

    if (eventTypeId && !editingEventType) {
        navigate('/eventTypes')
    }

    const columns: GridColDef[] = [
        {field: 'id', headerName: 'ID', width: 300},
        {field: 'name', headerName: 'Name', width: 250},
        {
            valueGetter: (input: GridValueGetterParams<EventType>) => input.row.recurrenceConfiguration.name,
            field: 'recurrenceConfiguration.name',
            headerName: 'Recurrence Config',
            width: 300
        },
        {
            field: 'participatingUsers',
            headerName: 'Participating Users',
            width: 300,
            valueGetter: (input: GridValueGetterParams<EventType>) => input.row.participatingUsers.length + " users"
        },
        {
            field: 'actions',
            type: 'actions',
            minWidth: 120,
            getActions: (params) => [
                <GridActionsCellItem
                    label="Edit"
                    icon={<EditIcon/>}
                    onClick={() => {
                        navigate('/eventTypes/' + params.row.id)
                    }}

                />,
                <GridActionsCellItem
                    label="Delete"
                    icon={<DeleteIcon/>}
                    onClick={() => deleteEventType(params.row)}

                />,
                <GridActionsCellItem
                    label="Create event from type"
                    icon={<AddIcon/>}
                    onClick={() => {
                        setActionLoading(true)
                        let newEventPromise = createEventFromEventType(params.row);
                        newEventPromise.then((newEvent) => {
                            navigate('/events/' + newEvent.id)
                            setActionLoading(false)
                        })
                    }}
                    showInMenu={true}
                />,
            ]
        },
    ];

    return (
        <>
            <Backdrop
                sx={{color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}}
                open={actionLoading}
            >
                <CircularProgress color="inherit"/>
            </Backdrop>
            <Box>
                <DataGrid
                    rows={eventTypes}
                    columns={columns}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 5},
                        },
                    }}
                    pageSizeOptions={[5, 10]}
                    checkboxSelection={false}
                    rowSelection={false}
                    loading={eventTypeLoading}
                    autoHeight={!eventTypeLoading}
                    slots={{
                        loadingOverlay: () => <LinearProgress/>,
                        noRowsOverlay: () => <Alert severity="info"> No event types found </Alert>
                    }}

                />
                {editingEventType &&
                    <ModifyEventTypeDialogue
                        open={true}
                        onClose={() => navigate("/eventTypes")}
                        saveChanges={updateEventType}
                        existingEventType={editingEventType}
                    />
                }
                {createEventTypeOpen &&
                    <CreateEventTypeDialogue
                        onClose={() => setCreateEventTypeOpen(false)}
                        addEventType={addEventType}
                    />
                }
                <Fab size="large"
                     color="primary"
                     onClick={() => setCreateEventTypeOpen(true)}
                     aria-label="add"
                     style={{
                         position: "absolute",
                         bottom: '5%',
                         right: '4%'
                     }}>
                    <AddIcon/>
                </Fab>
            </Box>
        </>
    );
}

export default EventTypesPage

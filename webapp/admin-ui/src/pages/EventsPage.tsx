import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import {useState} from "react";
import {DataGrid, GridActionsCellItem, GridColDef, GridValueGetterParams} from '@mui/x-data-grid';
import {EditEventDialogue, ModifyEventDialogue} from "../components/EditEventDialogue.tsx";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import useEventClient from "../client/EventClient.ts";
import {useNavigate, useParams} from "react-router-dom";
import {Event} from "../model/Event.ts";

function EventsPage() {
    const [events, addEvent, updateEvent, deleteEvent] = useEventClient()
    const [createEventOpen, setCreateEventOpen] = useState(false)
    const {eventId} = useParams()
    const navigate = useNavigate();

    const editingEvent: Event | null = eventId ? events.filter(event => event.id === eventId)[0] : null

    if (eventId && !editingEvent) {
        navigate('/events')
    }

    const columns: GridColDef[] = [
        {field: 'id', headerName: 'ID', width: 300},
        {field: 'name', headerName: 'Name', width: 250},
        {
            field: 'actions',
            type: 'actions',
            width: 80,
            getActions: (params) => [
                <GridActionsCellItem
                    label="Edit"
                    icon={<EditIcon/>}
                    onClick={() => {
                        navigate('/events/' + params.row.id)
                    }}

                />,
                <GridActionsCellItem
                    label="Delete"
                    icon={<DeleteIcon/>}
                    onClick={() => deleteEvent(params.row)}

                />,
            ]
        },
    ];

    return (
        <>
            <Box>
                <DataGrid
                    rows={events}
                    columns={columns}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 5},
                        },
                    }}
                    pageSizeOptions={[5, 10]}
                    checkboxSelection={false}
                    rowSelection={false}
                />
                {editingEvent &&
                    <ModifyEventDialogue
                        open={true}
                        onClose={() => navigate("/events")}
                        saveChanges={updateEvent}
                        existingEvent={editingEvent}
                    />
                }
                <EditEventDialogue open={createEventOpen} onClose={() => setCreateEventOpen(false)}
                                       addEvent={addEvent}/>
                <Fab size="large"
                     color="primary"
                     onClick={() => setCreateEventOpen(true)}
                     aria-label="add"
                     style={{
                         position: "absolute",
                         bottom: '5%',
                         right: '4%'
                     }}>
                    <PersonAddIcon/>
                </Fab>
            </Box>
        </>
    );
}

export default EventsPage

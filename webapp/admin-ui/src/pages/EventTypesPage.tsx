import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import {useState} from "react";
import {DataGrid, GridActionsCellItem, GridColDef, GridValueGetterParams} from '@mui/x-data-grid';
import {EditEventTypeDialogue, ModifyEventTypeDialogue} from "../components/EditEventTypeDialogue.tsx";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import useEventTypeClient from "../client/EventTypeClient.ts";
import {useNavigate, useParams} from "react-router-dom";
import {EventType} from "../model/EventType.ts";
import {Alert} from "@mui/material";

function EventTypesPage() {
    const [eventTypes, addEventType, updateEventType, deleteEventType, eventTypeError] = useEventTypeClient()
    const [createEventTypeOpen, setCreateEventTypeOpen] = useState(false)
    const {eventTypeId} = useParams()
    const navigate = useNavigate();

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
            field: 'actions',
            type: 'actions',
            width: 80,
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
            ]
        },
    ];

    return (
        <>
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
                />
                {editingEventType &&
                    <ModifyEventTypeDialogue
                        open={true}
                        onClose={() => navigate("/eventTypes")}
                        saveChanges={updateEventType}
                        existingEventType={editingEventType}
                    />
                }
                <EditEventTypeDialogue open={createEventTypeOpen} onClose={() => setCreateEventTypeOpen(false)}
                                       addEventType={addEventType}/>
                <Fab size="large"
                     color="primary"
                     onClick={() => setCreateEventTypeOpen(true)}
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

export default EventTypesPage

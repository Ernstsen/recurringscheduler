import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';
import {useState} from "react";
import {DataGrid, GridActionsCellItem, GridColDef} from '@mui/x-data-grid';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import useRecurrenceConfigurationClient from "../client/UseRecurrenceConfigurationClient.ts";
import {useNavigate, useParams} from "react-router-dom";
import {RecurrenceConfiguration} from "../model/RecurrenceConfiguration.ts";
import {
    CreateRecurrenceConfigurationDialogue,
    ModifyRecurrenceConfigurationDialogue
} from "../components/EditRecurrenceConfigurationDialogue.tsx";
import {Alert, LinearProgress} from "@mui/material";

function RecurrenceConfigurationsPage() {
    const [
        recurrenceConfigurations,
        addRecurrenceConfiguration,
        updateRecurrenceConfiguration,
        deleteRecurrenceConfiguration,
        recurrenceConfigurationError,
        recurrenceConfigurationLoading] = useRecurrenceConfigurationClient()
    const [createUserOpen, setCreateUserOpen] = useState(false)
    const {recurrenceConfigurationId} = useParams()
    const navigate = useNavigate();

    if (recurrenceConfigurationError) {
        return <Alert severity="error">Failed to read RecurrenceConfigurations from server</Alert>
    }

    const editingConfiguration: RecurrenceConfiguration | undefined = recurrenceConfigurationId ? recurrenceConfigurations.filter(recurrenceConfiguration => recurrenceConfiguration.id === recurrenceConfigurationId)[0] : undefined

    if (recurrenceConfigurationId && !editingConfiguration) {
        navigate('/recurrenceConfigurations')
    }

    const columns: GridColDef[] = [
        {field: 'id', headerName: 'ID', width: 300},
        {field: 'name', headerName: 'Name', width: 250},
        {field: 'timeUnit', headerName: 'Time Unit', width: 300},
        {field: 'occurrencesPerTimePeriod', headerName: 'Occurrences/Period', width: 300},
        {
            field: 'actions',
            type: 'actions',
            width: 80,
            getActions: (params) => [
                <GridActionsCellItem
                    label="Edit"
                    icon={<EditIcon/>}
                    onClick={() => {
                        navigate('/recurrenceConfigurations/' + params.row.id)
                    }}
                />,
                <GridActionsCellItem
                    label="Delete"
                    icon={<DeleteIcon/>}
                    onClick={() => deleteRecurrenceConfiguration(params.row)}

                />,
            ]
        },
    ];

    return (
        <>
            <Box>
                <DataGrid
                    rows={recurrenceConfigurations}
                    columns={columns}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 5},
                        },
                    }}
                    loading={recurrenceConfigurationLoading}
                    autoHeight={!recurrenceConfigurationLoading}
                    slots={{
                        loadingOverlay: () => <LinearProgress/>,
                        noRowsOverlay: () => <Alert severity="info"> No configurations found </Alert>
                    }}
                    pageSizeOptions={[5, 10]}
                    checkboxSelection={false}
                    rowSelection={false}
                />
                {editingConfiguration &&
                    <ModifyRecurrenceConfigurationDialogue
                        open={true}
                        onClose={() => navigate("/recurrenceConfigurations")}
                        saveChanges={updateRecurrenceConfiguration}
                        existingRecurrenceConfiguration={editingConfiguration}
                    />
                }
                <CreateRecurrenceConfigurationDialogue
                    open={createUserOpen}
                    onClose={() => setCreateUserOpen(false)}
                    addRecurrenceConfiguration={addRecurrenceConfiguration}
                />
                <Fab size="large"
                     color="primary"
                     onClick={() => setCreateUserOpen(true)}
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

export default RecurrenceConfigurationsPage

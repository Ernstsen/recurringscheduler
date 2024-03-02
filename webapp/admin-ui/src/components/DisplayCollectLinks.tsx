import * as React from "react";
import {Alert, Dialog, DialogContent, DialogContentText, DialogTitle, LinearProgress} from "@mui/material";
import {DataGrid, GridActionsCellItem, GridColDef, GridValueGetterParams} from "@mui/x-data-grid";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import useUserResponseClient from "../client/UserResponseClient.ts";
import {UserResponse} from "../model/UserResponse.ts";
import DeleteIcon from "@mui/icons-material/Delete";

interface DisplayCollectLinksProps {
    eventId: string,
    onClose: () => void,
}


const DisplayCollectLinks: React.FC<DisplayCollectLinksProps> = ({eventId, onClose}) => {
    const [userResponses, deleteUserResponse, userResponseError, userResponseLoading] = useUserResponseClient(eventId);

    const columns: GridColDef[] = [
        {field: 'id', headerName: 'ID', width: 300},
        {field: 'userEntityId', headerName: 'User', width: 300},
        {
            valueGetter: (input: GridValueGetterParams<UserResponse>) => input.row.chosenDates.length > 0 ? "Yes" : "No",
            field: 'chosenDates',
            headerName: 'Responded',
            width: 100
        },
        {
            field: 'actions',
            type: 'actions',
            width: 50,
            getActions: (params) => [
                <GridActionsCellItem
                    label="Delete"
                    icon={<DeleteIcon/>}
                    onClick={() => {
                        deleteUserResponse(params.row.id)
                    }}
                />
            ]
        }
    ];

    return (
        <Dialog open={true} onClose={onClose} fullWidth maxWidth={"md"}>
            <DialogTitle>Choose users to include</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Choose users to include in the event
                </DialogContentText>
                <DataGrid
                    disableRowSelectionOnClick
                    rows={userResponses}
                    columns={columns}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 5},
                        },
                    }}
                    loading={userResponseLoading}
                    autoHeight={!userResponseLoading}

                    slots={{
                        loadingOverlay: LinearProgress,
                        noRowsOverlay: () => <Alert severity="info"> No UserResponses found for event! </Alert>
                    }}
                    pageSizeOptions={[5, 10]}
                />
            </DialogContent>
            <DialogActions>
                <Button color="primary" onClick={() => {
                    onClose();
                }}>
                    Done
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default DisplayCollectLinks

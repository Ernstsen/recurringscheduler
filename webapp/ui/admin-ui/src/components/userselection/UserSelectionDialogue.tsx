import * as React from "react";
import {useState} from "react";
import {User} from "../../model/User.ts";
import {Alert, Dialog, DialogContent, DialogContentText, DialogTitle, LinearProgress} from "@mui/material";
import Button from "@mui/material/Button";
import DialogActions from "@mui/material/DialogActions";
import {DataGrid, GridColDef, GridRowSelectionModel} from "@mui/x-data-grid";
import useUserClient from "../../client/UserClient.ts";

interface UserSelectionDialogueProps {
    open: boolean,
    onClose: () => void,
    users: User[],
    setUsers: (users: User[]) => void
}

const UserSelectionDialogue: React.FC<UserSelectionDialogueProps> =
    ({
         open,
         onClose,
         users,
         setUsers
     }) => {
        const [existingUsers, , , , , userLoading] = useUserClient()
        const [internalUsers, setInternalUsers] = useState(users)


        const columns: GridColDef[] = [
            {field: 'id', headerName: 'ID', width: 60},
            {field: 'name', headerName: 'Name', width: 150},
            {field: 'email', headerName: 'Email', width: 300},
        ];

        return (
            <Dialog open={open} onClose={onClose} fullWidth maxWidth={"md"}>
                <DialogTitle>Choose users to include</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Choose users to include in the event
                    </DialogContentText>
                    <DataGrid
                        disableRowSelectionOnClick
                        rows={existingUsers}
                        columns={columns}
                        initialState={{
                            pagination: {
                                paginationModel: {page: 0, pageSize: 5},
                            },
                        }}
                        loading={userLoading}
                        autoHeight={!userLoading}
                        slots={{
                            loadingOverlay: LinearProgress,
                            noRowsOverlay: () => <Alert severity="info"> No users found </Alert>
                        }}
                        pageSizeOptions={[5, 10]}
                        checkboxSelection={true}
                        rowSelection={true}
                        onRowSelectionModelChange={(newRowSelectionModel: GridRowSelectionModel) => {
                            setInternalUsers(existingUsers.filter(user => newRowSelectionModel.includes(user.id || "")))
                        }}
                        rowSelectionModel={internalUsers.map(user => user.id || "")}
                    />
                </DialogContent>
                <DialogActions>
                    <Button color="error" onClick={onClose}>Cancel</Button>
                    <Button color="primary" onClick={() => {
                        setUsers(internalUsers);
                        onClose();
                    }}>
                        Save
                    </Button>
                </DialogActions>
            </Dialog>
        )
    }

export default UserSelectionDialogue;

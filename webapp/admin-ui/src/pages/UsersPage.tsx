import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';import {useState} from "react";
import {DataGrid, GridActionsCellItem, GridColDef} from '@mui/x-data-grid';
import {EditUserDialogue, ModifyUserDialogue} from "../components/EditUserDialogue.tsx";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import useUserClient from "../client/UserClient.ts";
import {useNavigate, useParams} from "react-router-dom";
import {User} from "../model/User.ts";
import {Alert} from "@mui/material";

function UsersPage() {
    const [users, addUser, updateUser, deleteUser, userError] = useUserClient()
    const [createUserOpen, setCreateUserOpen] = useState(false)
    const {userId} = useParams()
    const navigate = useNavigate();

    if (userError) {
        return <Alert severity="error">Failed to read Users from server</Alert>
    }

    const editingUser: User | null = userId ? users.filter(user => user.id === userId)[0] : null

    if(userId && !editingUser) {
        navigate('/users')
    }

    const columns: GridColDef[] = [
        {field: 'id', headerName: 'ID', width: 300},
        {field: 'name', headerName: 'Name', width: 250},
        {field: 'email', headerName: 'Email', width: 300},
        {
            field: 'actions',
            type: 'actions',
            width: 80,
            getActions: (params) => [
                <GridActionsCellItem
                    label="Edit"
                    icon={<EditIcon/>}
                    onClick={() => {
                        navigate('/users/' + params.row.id)
                    }}

                />,
                <GridActionsCellItem
                    label="Delete"
                    icon={<DeleteIcon/>}
                    onClick={() => deleteUser(params.row)}

                />,
            ]
        },
    ];

    return (
        <>
            <Box>
                <DataGrid
                    rows={users}
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
                {editingUser &&
                    <ModifyUserDialogue
                        open={true}
                        onClose={() => navigate("/users")}
                        saveChanges={updateUser}
                        existingUser={editingUser}
                    />
                }
                <EditUserDialogue open={createUserOpen} onClose={() => setCreateUserOpen(false)} addUser={addUser}/>
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

export default UsersPage

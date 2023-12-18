import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import {useState} from "react";
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import CreateUserDialogue from "../components/CreateUserDialogue.tsx";
import useUserClient from "../client/UserClient.ts";

const columns: GridColDef[] = [
    {field: 'id', headerName: 'ID', width: 300},
    {field: 'name', headerName: 'Name', width: 130},
    {field: 'email', headerName: 'Email', width: 130},
];

function Console() {
    const [users, addUser] = useUserClient()
    // const [users, setUsers] = useState([])
    const [createUserOpen, setCreateUserOpen] = useState(false)

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
                    checkboxSelection
                />
                <CreateUserDialogue open={createUserOpen} onClose={() => setCreateUserOpen(false)} addUser={addUser}/>
                <Fab size="large" color="primary" onClick={() => setCreateUserOpen(true)} aria-label="add"
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

export default Console

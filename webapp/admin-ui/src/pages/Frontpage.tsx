import './Frontpage.css'
import Button from '@mui/material/Button';
import {useNavigate} from "react-router-dom";


function Frontpage() {
    let navigate = useNavigate();

    return (
        <>
            <div className="card">
                <Button variant="contained" onClick={() => navigate("/console")}>
                    Enter Admin Panel
                </Button>
            </div>
            <p className="read-the-docs">
                Press the button to enter the site
            </p>
        </>
    )
}

export default Frontpage

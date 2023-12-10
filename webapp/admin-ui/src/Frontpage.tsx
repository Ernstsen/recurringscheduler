import './App.css'
import Button from '@mui/material/Button';


function Frontpage() {
    return (
        <>
            <h1>RecurringScheduler Administration Panel</h1>
            <div className="card">
                <Button variant="contained" href="/console">
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

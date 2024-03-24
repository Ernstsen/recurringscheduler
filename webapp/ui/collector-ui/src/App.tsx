import React from 'react'
import {useParams} from "react-router-dom";
import {CollectView} from "./CollectView.tsx";

function App() {
    let {collectId} = useParams();

    return (
        <React.Fragment>
            {collectId ? <CollectView collectId={collectId}/> : <h1>Must supply collect ID</h1>  }
        </React.Fragment>
    )
}

export default App

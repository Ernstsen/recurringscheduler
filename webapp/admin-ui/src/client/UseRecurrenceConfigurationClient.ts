import {RecurrenceConfiguration} from "../model/RecurrenceConfiguration.ts";
import {useEffect, useState} from "react";

export default function useRecurrenceConfigurationClient(): [
    recurrenceConfigurations: RecurrenceConfiguration[],
    addRecurrenceConfiguration: (recurrenceConfiguration: RecurrenceConfiguration) => void,
    updateRecurrenceConfiguration: (recurrenceConfiguration: RecurrenceConfiguration) => void,
    deleteRecurrenceConfiguration: (recurrenceConfiguration: RecurrenceConfiguration) => void] {
    const [recurrenceConfigurations, setRecurrenceConfigurations] = useState<RecurrenceConfiguration[]>([])

    useEffect(() => {
        fetch('/api/recurrenceConfigurations').then(response => response.json())
            .then(data => {
                setRecurrenceConfigurations(data)
                console.log(data)
            })
    }, [])

    const addRecurrenceConfiguration = (recurrenceConfiguration: RecurrenceConfiguration) => {
        fetch('/api/recurrenceConfigurations', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(recurrenceConfiguration)
        }).then(response => response.json())
            .then(data => {
                setRecurrenceConfigurations([...recurrenceConfigurations, data])
            })
    }

    const updateRecurrenceConfiguration = (recurrenceConfiguration: RecurrenceConfiguration) => {
        fetch('/api/recurrenceConfigurations/' + recurrenceConfiguration.id + "/", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(recurrenceConfiguration)
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    throw new Error("Failed to update recurrenceConfiguration: " + response.status)
                }
            })
            .then(data => {
                setRecurrenceConfigurations([...recurrenceConfigurations.filter(u => u.id !== recurrenceConfiguration.id), data])
            })
            .catch(error => console.log("Failed to update recurrenceConfiguration", error))
    }

    const deleteRecurrenceConfiguration = (recurrenceConfiguration: RecurrenceConfiguration) => {
        console.log("Deleting recurrenceConfiguration: ", recurrenceConfiguration)
        fetch('/api/recurrenceConfigurations/' + recurrenceConfiguration.id + "/", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(recurrenceConfiguration)
        }).then(response => {
            if (response.ok) {
                setRecurrenceConfigurations(recurrenceConfigurations.filter(u => u.id !== recurrenceConfiguration.id))
            } else {
                throw new Error("Failed to delete recurrenceConfiguration: " + response.status)
            }
        }).catch(error => console.log("Failed to delete recurrenceConfiguration", error))
    }

    return [recurrenceConfigurations, addRecurrenceConfiguration, updateRecurrenceConfiguration, deleteRecurrenceConfiguration]
}

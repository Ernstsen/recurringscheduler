import {RecurrenceConfiguration} from "../model/RecurrenceConfiguration.ts";
import {useEffect, useState} from "react";

export default function useRecurrenceConfigurationClient(): [
    recurrenceConfigurations: RecurrenceConfiguration[],
    addRecurrenceConfiguration: (recurrenceConfiguration: RecurrenceConfiguration) => void,
    updateRecurrenceConfiguration: (recurrenceConfiguration: RecurrenceConfiguration) => void,
    deleteRecurrenceConfiguration: (recurrenceConfiguration: RecurrenceConfiguration) => void,
    recurrenceConfigurationError: boolean,
    recurrenceConfigurationLoading: boolean
] {
    const [recurrenceConfigurations, setRecurrenceConfigurations] = useState<RecurrenceConfiguration[]>([])
    const [recurrenceConfigurationError, setRecurrenceConfigurationError] = useState(false)
    const [recurrenceConfigurationLoading, setRecurrenceConfigurationLoading] = useState(true)

    useEffect(() => {
        fetch('/api/recurrenceConfigurations').then(response => {
            if (response.ok) {
                return response.json()
            } else {
                setRecurrenceConfigurationError(true)
                throw new Error("Failed to fetch recurrenceConfigurations: " + response.status)
            }
        }).then(data => {
            setRecurrenceConfigurations(data)
            setRecurrenceConfigurationLoading(false)
        })
    }, [])

    const addRecurrenceConfiguration = (recurrenceConfiguration: RecurrenceConfiguration) => {
        fetch('/api/recurrenceConfigurations', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(recurrenceConfiguration)
        }).then(response => {
            if (response.ok) {
                return response.json()
            } else {
                setRecurrenceConfigurationError(true)
                throw new Error("Failed to create recurrenceConfiguration: " + response.status)
            }
        }).then(data => {
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
                    setRecurrenceConfigurationError(true)
                    throw new Error("Failed to update recurrenceConfiguration: " + response.status)
                }
            })
            .then(data => {
                setRecurrenceConfigurations([...recurrenceConfigurations.filter(u => u.id !== recurrenceConfiguration.id), data])
            })
            .catch(error => console.log("Failed to update recurrenceConfiguration", error))
    }

    const deleteRecurrenceConfiguration = (recurrenceConfiguration: RecurrenceConfiguration) => {
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
                setRecurrenceConfigurationError(true)
                throw new Error("Failed to delete recurrenceConfiguration: " + response.status)
            }
        }).catch(error => console.log("Failed to delete recurrenceConfiguration", error))
    }

    return [
        recurrenceConfigurations,
        addRecurrenceConfiguration,
        updateRecurrenceConfiguration,
        deleteRecurrenceConfiguration,
        recurrenceConfigurationError,
        recurrenceConfigurationLoading
    ]
}

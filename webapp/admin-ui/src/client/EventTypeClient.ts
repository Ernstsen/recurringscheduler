import {EventType} from "../model/EventType.ts";
import {useEffect, useState} from "react";

export default function useEventTypeClient(): [
    eventTypes: EventType[],
    addEventType: (eventType: EventType) => void,
    updateEventType: (eventType: EventType) => void,
    deleteEventType: (eventType: EventType) => void,
    eventTypeError: boolean,
    eventTypeLoading: boolean,
] {
    const [eventTypes, setEventTypes] = useState<EventType[]>([])
    const [eventTypeError, setEventTypeError] = useState(false)
    const [eventTypeLoading, setEventTypeLoading] = useState(true)

    useEffect(() => {
        fetch('/api/eventTypes').then(response => {
            if (response.ok) {
                return response.json()
            } else {
                setEventTypeError(true)
                throw new Error("Failed to fetch eventTypes: " + response.status)
            }
        }).then(data => {
            setEventTypes(data)
            setEventTypeLoading(false)
        })
    }, [])

    const addEventType = (eventType: EventType) => {
        fetch('/api/eventTypes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(eventType)
        }).then(response => {
            if (response.ok) {
                return response.json()
            } else {
                setEventTypeError(true)
                throw new Error("Failed to create eventType: " + response.status)
            }
        }).then(data => {
            setEventTypes([...eventTypes, data])
        })
    }

    const updateEventType = (eventType: EventType) => {
        fetch('/api/eventTypes/' + eventType.id + "/", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(eventType)
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    setEventTypeError(true)
                    throw new Error("Failed to update eventType: " + response.status)
                }
            })
            .then(data => {
                setEventTypes([...eventTypes.filter(u => u.id !== eventType.id), data])
            })
            .catch(error => console.log("Failed to update eventType", error))
    }

    const deleteEventType = (eventType: EventType) => {
        console.log("Deleting eventType: ", eventType)
        fetch('/api/eventTypes/' + eventType.id + "/", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(eventType)
        }).then(response => {
            if (response.ok) {
                setEventTypes(eventTypes.filter(u => u.id !== eventType.id))
            } else {
                setEventTypeError(true)
                throw new Error("Failed to delete eventType: " + response.status)
            }
        }).catch(error => console.log("Failed to delete eventType", error))
    }

    return [eventTypes, addEventType, updateEventType, deleteEventType, eventTypeError, eventTypeLoading]
}

import {EventType} from "../model/EventType.ts";
import {useEffect, useState} from "react";

export default function useEventTypeClient(): [
    eventTypes: EventType[],
    addEventType: (eventType: EventType) => void,
    updateEventType: (eventType: EventType) => void,
    deleteEventType: (eventType: EventType) => void] {
    const [eventTypes, setEventTypes] = useState<EventType[]>([])

    useEffect(() => {
        fetch('/api/eventTypes').then(response => response.json())
            .then(data => {
                setEventTypes(data)
            })
    }, [])

    const addEventType = (eventType: EventType) => {
        fetch('/api/eventTypes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(eventType)
        }).then(response => response.json())
            .then(data => {
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
                throw new Error("Failed to delete eventType: " + response.status)
            }
        }).catch(error => console.log("Failed to delete eventType", error))
    }

    return [eventTypes, addEventType, updateEventType, deleteEventType]
}

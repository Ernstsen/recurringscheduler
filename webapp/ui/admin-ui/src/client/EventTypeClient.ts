import {EventType} from "../model/EventType.ts";
import {Event} from "../model/Event.ts";
import {useEffect, useState} from "react";
import {useAuth} from "../authentication/UseAuthentication.tsx";

export const useCreateEventFromEventType = (): (eventType: EventType) => Promise<Event> => {
    const {authentication} = useAuth();

    if (!authentication) {
        throw new Error("No authentication found")
    }

    return (eventType: EventType): Promise<Event> => {
        return fetch('/api/eventTypes/' + eventType.id + "/createEvent", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authentication.token
            },
        }).then(response => {
            if (response.ok) {
                return response.json()
            } else {
                throw new Error("Failed to create event: " + response.status)
            }
        })

    }
}

export default function useEventTypeClient(): [
    eventTypes: EventType[],
    addEventType: (eventType: EventType) => void,
    updateEventType: (eventType: EventType) => void,
    deleteEventType: (eventType: EventType) => void,
    eventTypeError: boolean,
    eventTypeLoading: boolean,
] {
    const {authentication} = useAuth();
    const [eventTypes, setEventTypes] = useState<EventType[]>([])
    const [eventTypeError, setEventTypeError] = useState(false)
    const [eventTypeLoading, setEventTypeLoading] = useState(true)

    if (!authentication) {
        console.log("No authentication found, returning empty eventType list")
        return [eventTypes, () => {
        }, () => {
        }, () => {
        }, true, false]
    }

    const authenticationToken = authentication.token

    useEffect(() => {
        fetch('/api/eventTypes', {headers: {'Authorization': 'Bearer ' + authenticationToken}}).then(response => {
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
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authenticationToken
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
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authenticationToken
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
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authenticationToken
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

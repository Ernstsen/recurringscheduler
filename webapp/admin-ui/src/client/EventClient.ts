import {Event} from "../model/Event.ts";
import {useEffect, useState} from "react";
import {useAuth} from "../authentication/UseAuthentication.tsx";
import {stringToDate} from "./DateUtils.ts";

const deserializeDatesInIncomingEvent = (event: any): Event => {
    event.possibleTimes = event.possibleTimes
        ?.map(stringToDate)
        ?.sort((a: Date, b: Date) => a.getTime() - b.getTime())
    event.chosenTime = event.chosenTime ? stringToDate(event.chosenTime) : null
    return event
}

export default function useEventClient(): [
    events: Event[],
    addEvent: (event: Event) => void,
    updateEvent: (event: Event) => void,
    deleteEvent: (event: Event) => void,
    eventError: boolean,
    eventLoading: boolean,
    createResponses: (event: Event) => void
] {
    const {authentication} = useAuth();
    const [events, setEvents] = useState<Event[]>([])
    const [eventError, setEventError] = useState(false)
    const [eventLoading, setEventLoading] = useState(true)

    if (!authentication) {
        console.log("No authentication found, returning empty event list")
        return [events,
            () => {
            },
            () => {
            },
            () => {
            },
            true, false,
            () => {
            }
        ]
    }
    const authenticationToken = authentication.token

    useEffect(() => {
        fetch('/api/events', {headers: {'Authorization': 'Bearer ' + authenticationToken}})
            .then(response => response.json())
            .then(data => {
                setEvents(data.map(deserializeDatesInIncomingEvent))
                setEventLoading(false)
            }).catch(() => {
            setEventError(true)

        })
    }, [])

    const addEvent = (event: Event): void => {
        fetch('/api/events', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authenticationToken
            },
            body: JSON.stringify(event)
        }).then(response => {
            if (response.ok) {
                return response.json()
            } else {
                setEventError(true)
                throw new Error("Failed to create event: " + response.status)
            }
        }).then(data => {
            setEvents([...events, deserializeDatesInIncomingEvent(data)])
        })
    }

    const updateEvent = (event: Event) => {
        console.log("Updating event", event)
        fetch('/api/events/' + event.id + "/", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authenticationToken
            },
            body: JSON.stringify(event)
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    setEventError(true)
                    throw new Error("Failed to update event: " + response.status)

                }
            })
            .then(data => {
                setEvents([...events.filter(u => u.id !== event.id), deserializeDatesInIncomingEvent(data)])
            })
    }

    const deleteEvent = (event: Event) => {
        console.log("Deleting event: ", event)
        fetch('/api/events/' + event.id + "/", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authenticationToken
            },
            body: JSON.stringify(event)
        }).then(response => {
            if (response.ok) {
                setEvents(events.filter(u => u.id !== event.id))
            } else {
                setEventError(true)
                throw new Error("Failed to delete event: " + response.status)
            }
        })
    }

    const createResponses = (event: Event): void => {
        fetch('/api/userResponse/events/' + event.id, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authenticationToken
            },
            body: JSON.stringify(event)
        }).then(response => {
            if (response.ok) {
                return response.json()
            } else {
                throw new Error("Failed to create user responses for event: " + response.status)
            }
        })
    }

    return [events, addEvent, updateEvent, deleteEvent, eventError, eventLoading, createResponses]
}

import {Event} from "../model/Event.ts";
import {useEffect, useState} from "react";

export default function useEventClient(): [
    events: Event[],
    addEvent: (event: Event) => void,
    updateEvent: (event: Event) => void,
    deleteEvent: (event: Event) => void] {
    const [events, setEvents] = useState<Event[]>([])

    useEffect(() => {
        fetch('/api/events').then(response => response.json())
            .then(data => {
                setEvents(data)
                console.log(data)
            })
    }, [])

    const addEvent = (event: Event) => {
        fetch('/api/events', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        }).then(response => response.json())
            .then(data => {
                setEvents([...events, data])
            })
    }

    const updateEvent = (event: Event) => {
        fetch('/api/events/' + event.id + "/", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    throw new Error("Failed to update event: " + response.status)
                }
            })
            .then(data => {
                setEvents([...events.filter(u => u.id !== event.id), data])
            })
            .catch(error => console.log("Failed to update event", error))
    }

    const deleteEvent = (event: Event) => {
        console.log("Deleting event: ", event)
        fetch('/api/events/' + event.id + "/", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        }).then(response => {
            if (response.ok) {
                setEvents(events.filter(u => u.id !== event.id))
            } else {
                throw new Error("Failed to delete event: " + response.status)
            }
        }).catch(error => console.log("Failed to delete event", error))
    }

    return [events, addEvent, updateEvent, deleteEvent]
}

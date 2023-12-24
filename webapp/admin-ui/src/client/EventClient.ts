import {Event} from "../model/Event.ts";
import {useEffect, useState} from "react";

const stringToDate = (dateString: string): Date => {
    let [year, month, day] = dateString.split("-")
    return new Date(parseInt(year), parseInt(month) - 1, parseInt(day), 12)
}
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
    deleteEvent: (event: Event) => void] {
    const [events, setEvents] = useState<Event[]>([])

    useEffect(() => {
        fetch('/api/events').then(response => response.json())
            .then(data => {
                setEvents(data.map(deserializeDatesInIncomingEvent))
            })
    }, [])

    const addEvent = (event: Event) => {
        console.log("Creating event", event)
        fetch('/api/events', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        }).then(response => response.json())
            .then(data => {
                setEvents([...events, deserializeDatesInIncomingEvent(data)])
            })
    }

    const updateEvent = (event: Event) => {
        console.log("Updating event", event)
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
                setEvents([...events.filter(u => u.id !== event.id), deserializeDatesInIncomingEvent(data)])
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

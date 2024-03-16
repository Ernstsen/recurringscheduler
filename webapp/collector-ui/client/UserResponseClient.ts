import {Event} from "../model/Event";
import {useEffect, useState} from "react";
import {UserResponse} from "../model/UserResponse";

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
export default function useUserResponseClient(responseKey: string): [
    userResponse: UserResponse | undefined,
    updateResponse: (userResponse: UserResponse) => void,
    error: boolean,
    loading: boolean,
] {
    const [userResponse, setUserResponse] = useState<UserResponse | undefined>(undefined)
    const [error, setError] = useState(false)
    const [loading, setLoading] = useState(true)


    useEffect(() => {
            fetch('/api/userResponse/' + responseKey)
                .then(response => {
                    if (!response.ok) {
                        setLoading(false)
                        setError(true)
                    } else {
                        return response.json()
                    }
                })
                .then(data => {
                    setUserResponse(data)
                    data.event = deserializeDatesInIncomingEvent(data.event)
                    setLoading(false)
                }, () => {
                    setError(true)
                    setLoading(false)
                })
                .catch(() => setError(true))
        }
        , [responseKey])

    const updateResponse = (userResponse: UserResponse) => {
        console.log("Updating user response", userResponse)
        fetch('/api/userResponse/' + userResponse.id + "/", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userResponse)
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    setError(true)
                    throw new Error("Failed to update user response: " + response.status)

                }
            })
            .then(_ => {
                setUserResponse(userResponse)
            })
    }

    return [userResponse, updateResponse, error, loading]
}

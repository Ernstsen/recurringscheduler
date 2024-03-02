import {UserResponse} from "../model/UserResponse.ts";
import {useEffect, useState} from "react";
import {useAuth} from "../authentication/UseAuthentication.tsx";
import {stringToDate} from "./DateUtils.ts";

const deserializeDatesInIncomingEvent = (userRespoonse: any): UserResponse => {
    userRespoonse.chosenDates = userRespoonse.chosenDates
        ?.map(stringToDate)
        ?.sort((a: Date, b: Date) => a.getTime() - b.getTime())
    return userRespoonse
}

export default function useUserResponseClient(eventId: string): [
    userResponses: UserResponse[],
    deleteUserResponse: (userResponse: string) => void,
    userResponseError: boolean,
    userResponseLoading: boolean,
] {
    const {authentication} = useAuth();
    const [userResponses, setUserResponses] = useState<UserResponse[]>([])
    const [userResponseError, setUserResponseError] = useState(false)
    const [userResponseLoading, setUserResponseLoading] = useState(true)

    if (!authentication) {
        console.log("No authentication found, returning empty event list")
        return [userResponses, () => {
        }, true, false]
    }
    const authenticationToken = authentication.token

    useEffect(() => {
        fetch('/api/userResponse/events/' + eventId, {headers: {'Authorization': 'Bearer ' + authenticationToken}})
            .then(response => response.json())
            .then(data => {
                setUserResponses(data.map(deserializeDatesInIncomingEvent))
                setUserResponseLoading(false)
            }).catch(() => {
            setUserResponseError(true)

        })
    }, [eventId])

    const deleteUserResponse = (userResponseId: string) => {
        fetch('/api/userResponse/' + userResponseId + "/", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authenticationToken
            },
        }).then(response => {
            if (response.ok) {
                setUserResponses(userResponses.filter(u => u.id !== userResponseId))
            } else {
                setUserResponseError(true)
                throw new Error("Failed to delete userResponse: " + response.status)
            }
        })
    }

    return [userResponses, deleteUserResponse, userResponseError, userResponseLoading]
}

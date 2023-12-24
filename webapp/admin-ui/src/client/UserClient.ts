import {User} from "../model/User.ts";
import {useEffect, useState} from "react";

export default function useUserClient(): [
    users: User[],
    addUser: (user: User) => void,
    updateUser: (user: User) => void,
    deleteUser: (user: User) => void,
    userError: boolean
] {
    const [users, setUsers] = useState<User[]>([])
    const [userError, setUserError] = useState(false)

    useEffect(() => {
        fetch('/api/users')
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    setUserError(true)
                    throw new Error("Failed to fetch users: " + response.status)
                }
            })
            .then(data => {
                setUsers(data)
            })
    }, [])

    const addUser = (user: User) => {
        fetch('/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    setUserError(true)
                    throw new Error("Failed to create user: " + response.status)
                }
            })
            .then(data => {
                setUsers([...users, data])
            })
    }

    const updateUser = (user: User) => {
        fetch('/api/users/' + user.id + "/", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    setUserError(true)
                    throw new Error("Failed to update user: " + response.status)
                }
            })
            .then(data => {
                setUsers([...users.filter(u => u.id !== user.id), data])
            })
            .catch(error => console.log("Failed to update user", error))
    }

    const deleteUser = (user: User) => {
        console.log("Deleting user: ", user)
        fetch('/api/users/' + user.id + "/", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        }).then(response => {
            if (response.ok) {
                setUsers(users.filter(u => u.id !== user.id))
            } else {
                setUserError(true)
                throw new Error("Failed to delete user: " + response.status)
            }
        }).catch(error => console.log("Failed to delete user", error))
    }

    return [users, addUser, updateUser, deleteUser, userError]
}

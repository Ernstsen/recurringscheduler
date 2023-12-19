import {User} from "../model/User.ts";
import {useEffect, useState} from "react";

export default function useUserClient(): [
    users: User[],
    addUser: (user: User) => void,
    updateUser: (user: User) => void,
    deleteUser: (user: User) => void] {
    const [users, setUsers] = useState<User[]>([])

    useEffect(() => {
        fetch('/api/users').then(response => response.json())
            .then(data => {
                setUsers(data)
                console.log(data)
            })
    }, [])

    const addUser = (user: User) => {
        fetch('/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        }).then(response => response.json())
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
                    throw new Error("Failed to update user: " + response.status)
                }
            })
            .then(data => {
                setUsers([...users.filter(u => u.id !== user.id), data])
            })
            .catch(error => console.log("Failed to update user", error))
    }

    const deleteUser = (user: User) => {
        fetch('/api/users/' + user.id + "/", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        }).then(response => response.json())
            .then(data => {
                console.log("Deleting user: " + user.toString(), data)
                setUsers(users.filter(u => u.id !== user.id))
            })
    }

    return [users, addUser, updateUser, deleteUser]
}

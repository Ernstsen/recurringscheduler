import {AuthenticationResponse} from "../model/AuthenticationResponse.ts";

export default function useAuthenticationClient(): [
    doLogin: (email: string, password: string) => Promise<AuthenticationResponse>,
] {
    const doLogin = async (email: string, password: string): Promise<AuthenticationResponse> => {
        return fetch('/api/authentication/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({email: email, password: password})
        }).then(response => {
            if (response.ok) {
                return response.json()
            } else {
                return Promise.reject("Failed to login: " + response.statusText)
            }
        })
    }

    return [doLogin]
}

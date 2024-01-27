import {AuthenticationInformation} from "../model/AuthenticationInformation.ts";

export default function useAuthenticationClient(): [
    doLogin: (email: string, password: string) => Promise<AuthenticationInformation>,
] {
    const doLogin = async (email: string, password: string): Promise<AuthenticationInformation> => {
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

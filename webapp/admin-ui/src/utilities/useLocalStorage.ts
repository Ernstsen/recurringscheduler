import {useState} from "react";

export function useLocalStorage<T>(keyName: string, defaultValue: T | undefined):
    [T | undefined, (newValue: T | undefined) => void] {

    function readFromWindow(): T | undefined {
        try {
            const value = window.localStorage.getItem(keyName);
            if (value) {
                let parse = JSON.parse(value);
                console.log("Found value for key " + keyName + " in local storage: " + value);
                return parse as T;
            } else {
                console.log("No value found for key " + keyName + " in local storage, using default value");
                // window.localStorage.setItem(keyName, JSON.stringify(defaultValue));
                return defaultValue;
            }
        } catch (err) {
            return undefined;
        }
    }

    let stateFromWindow: T | undefined = readFromWindow();
    const [storedValue, setStoredValue] = useState<T | undefined>(stateFromWindow);

    const setValue = (newValue: T | undefined) => {
        try {
            console.log("Setting new value: ", newValue)
            window.localStorage.setItem(keyName, JSON.stringify(newValue));
            setStoredValue(defaultValue);
        } catch (err) {
            throw new Error("Failed to store value in local storage: " + err);
        }
    };
    return [storedValue, setValue];
}
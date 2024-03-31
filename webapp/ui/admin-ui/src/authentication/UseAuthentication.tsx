import {Context, createContext, FC, ReactNode, useContext, useMemo} from "react";
import {useLocalStorage} from "../utilities/useLocalStorage.ts";
import useAuthenticationClient from "../client/AuthenticationClient.ts";
import {AuthenticationInformation} from "../model/AuthenticationInformation.ts";

// @ts-ignore
export const AuthContext: Context<AuthControl> = createContext();

export interface AuthControl {
    authentication: AuthenticationInformation | undefined,
    login: (email: string, password: string) => Promise<void>,
    logout: () => void
}

interface AuthProviderProps {
    children?: ReactNode
}

export const AuthProvider: FC<AuthProviderProps> = ({children}) => {
    const [user, setUser] = useLocalStorage<AuthenticationInformation>("user", undefined);
    const [doLogin] = useAuthenticationClient()

    // call this function when you want to authenticate the user
    const login = async (email: string, password: string): Promise<void> => {
        return doLogin(email, password)
            .then((loggedInResponse) => {
                setUser(loggedInResponse);
                return Promise.resolve();
            }, (error) => {
                return Promise.reject(error);
            })
    };

    const logout = () => {
        setUser(undefined);
    };

    const value: AuthControl = useMemo(
        () => ({
            authentication: user,
            login,
            logout
        }),
        [user]
    );

    console.log("AuthProvider", value);

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    return useContext(AuthContext);
};

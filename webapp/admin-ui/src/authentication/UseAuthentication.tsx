import {Context, createContext, FC, ReactNode, useContext, useMemo} from "react";
import {useNavigate} from "react-router-dom";
import {useLocalStorage} from "../utilities/useLocalStorage.ts";
import {User} from "../model/User.ts";

// @ts-ignore
const AuthContext: Context<AuthControl> = createContext();

export interface AuthControl {
    user: User | null,
    login: (data: User) => void,
    logout: () => void
}

// https://blog.logrocket.com/complete-guide-authentication-with-react-router-v6/

interface AuthProviderProps {
    children?: ReactNode
}

export const AuthProvider : FC<AuthProviderProps> = ({children}) => {
    const [user, setUser] = useLocalStorage("user", null);
    const navigate = useNavigate();

    // call this function when you want to authenticate the user
    const login = async (data: any) => {
        setUser(data);
        navigate("/");
    };

    // call this function to sign out logged in user
    const logout = () => {
        setUser(null);
        navigate("/Login", {replace: true});
    };

    const value : AuthControl = useMemo(
        () => ({
            user,
            login,
            logout
        }),
        [user]
    );

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    return useContext(AuthContext);
};
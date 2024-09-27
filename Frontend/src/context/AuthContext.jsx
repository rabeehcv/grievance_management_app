import { createContext, useState } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    const login = (credentials) => {
        setUser(credentials);
    };

    const logout = () => {
        setUser(null);
    };

    return (
        <AuthContext.Provider value = {{user, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;
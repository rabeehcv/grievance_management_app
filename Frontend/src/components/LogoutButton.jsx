import AuthContext from '../context/AuthContext.jsx';
import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';

const LogoutButton = () => {
    const { logout } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogoutButton = () => {
        logout();
        navigate("/login");
    };

    return (
        <div>
            <button onClick={handleLogoutButton} style ={{position: 'absolute', 
                top: '10px', 
                right: '10px', 
                padding: '10px 20px',
                backgroundColor: '#f00', 
                color: '#fff', 
                border: 'none', 
                borderRadius: '5px',
                cursor: 'pointer'}}>Logout</button>
        </div>
    );
};

export default LogoutButton;
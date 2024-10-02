import {useNavigate} from 'react-router-dom';
import '../styles/UserTab.css';

const UserTab = () => {
    const navigate = useNavigate();
    const handleNavigation = (path) => {
        navigate(path);
    }
    return (
        <div className="user-tab-container">
                <button className="tab-button" onClick ={() => handleNavigation('/user/grievance')}>Grievance Submission</button>
                <button className="tab-button" onClick ={() => handleNavigation("/user/status")}>Status</button>
                <button className="tab-button" onClick ={() => handleNavigation("/user/account")}>Account</button>
            </div>
    )
}

export default UserTab;
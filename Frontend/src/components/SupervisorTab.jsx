import {useNavigate} from 'react-router-dom';
import '../styles/SupervisorTab.css';

const SupervisorTab = () => {
    const navigate = useNavigate();
    const handleNavigation = (path) => {
        navigate(path);
    }
    return (
        <div className="supervisor-tab-container">
            <button className="supervisor-tab-button" onClick ={() => handleNavigation('/supervisor/unassignedGrievances')}>Unassigned Grievances</button>
            <button className="supervisor-tab-button" onClick ={() => handleNavigation("/supervisor/assignedGrievances")}>Status</button>
            <button className="supervisor-tab-button" onClick ={() => handleNavigation("/user/account")}>Account</button>
        </div>
    )
}

export default SupervisorTab;
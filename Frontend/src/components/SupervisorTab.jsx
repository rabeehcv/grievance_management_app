import {useNavigate} from 'react-router-dom';

const SupervisorTab = () => {
    const navigate = useNavigate();
    const handleNavigation = (path) => {
        navigate(path);
    }
    return (
        <div style = {{ marginTop : '20px'}}>
            <button onClick ={() => handleNavigation('/supervisor/unassignedGrievances')}>Unassigned Grievances</button>
            <button onClick ={() => handleNavigation("/supervisor/assignedGrievances")}>Status</button>
            <button onClick ={() => handleNavigation("/user/account")}>Account</button>
        </div>
    )
}

export default SupervisorTab;
import {useNavigate} from 'react-router-dom';
import '../styles/AssigneeTab.css';

const AssigneeTab = () => {
    const navigate = useNavigate();

    const handleNavigation = (path) => {
        navigate(path);
    };

    return(
        <div className="assignee-tab-container">
            <button className="assignee-tab-button" onClick={() => handleNavigation("/assignee/assignedGrievances")}>Assigned Grievances</button>
            <button className="assignee-tab-button" onClick={() => handleNavigation("/user/account")}>Account</button>
        </div>
    );
};

export default AssigneeTab;
import {useNavigate} from 'react-router-dom';

const AssigneeTab = () => {
    const navigate = useNavigate();

    const handleNavigation = (path) => {
        navigate(path);
    };

    return(
        <div style = {{ marginTop : '20px'}}>
            <button onClick={() => handleNavigation("/assignee/assignedGrievances")}>Assigned Grievances</button>
            <button onClick={() => handleNavigation("/user/account")}>Account</button>
        </div>
    );
};

export default AssigneeTab;
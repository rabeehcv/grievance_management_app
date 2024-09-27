import {useNavigate} from 'react-router-dom';

const UserTab = () => {
    const navigate = useNavigate();
    const handleNavigation = (path) => {
        navigate(path);
    }
    return (
        <div style = {{ marginTop : '20px'}}>
                <button onClick ={() => handleNavigation('/user/grievance')}>Grievance Submission</button>
                <button onClick ={() => handleNavigation("/user/status")}>Status</button>
                <button onClick ={() => handleNavigation("/user/account")}>Account</button>
            </div>
    )
}

export default UserTab;
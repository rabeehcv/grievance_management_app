import AuthContext from '../context/AuthContext.jsx';
import {useState, useContext} from 'react';
import {useNavigate} from 'react-router-dom';
import LogoutButton from './LogoutButton.jsx';
import SupervisorTab from './SupervisorTab.jsx';

const UnassignedGrievances = () => {
    const {user} = useContext(AuthContext);
    return (
        <div>
            <LogoutButton />
            <h2>Unassigned Grievances</h2>
            <SupervisorTab />
        </div>
    )
}

export default UnassignedGrievances;
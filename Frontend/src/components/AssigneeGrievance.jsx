import LogoutButton from './LogoutButton.jsx';
import AssigneeTab from './AssigneeTab.jsx';
import {useState, useContext, useEffect} from 'react';
import AuthContext from '../context/AuthContext.jsx';
import axios from 'axios';
import '../styles/AssigneeGrievance.css';

const AssigneeGrievance = () => {
    const [grievances, setGrievances] = useState([]);
    const { user } = useContext(AuthContext);
    const [status, setStatus] = useState('PENDING');
    const [editingField, setEditingField] = useState(null);
    const [showUserDetails, setShowUserDetails] = useState({});

    useEffect(() => {
        const fetchGrievance = async() => {
            try {
                const response = await axios.get('http://localhost:8083/grievance/assignee/assignedGrievances', {
                    auth : {
                        username : user.email,
                        password : user.password
                    }
                });
                setGrievances(response.data.data);
            } catch(error) {
                console.error("Fetching greivances failed.", error);
            }
        };
        fetchGrievance();
    }, [user]);

    const handleEditingField = (grievanceId) => {
        setEditingField(grievanceId);
    }

    const toggleUserDetails = (grievanceId) => {
        setShowUserDetails(prevState => ({
            ...prevState,
            [grievanceId] : !prevState[grievanceId]
        }));
    };

    const handleStatus = async(grievanceId) => {
        try {
            const updateStatus = {grievanceId,status}
            const response = await axios.patch(`http://localhost:8083/grievance/assignee/updateStatus`, updateStatus,{
                auth : {
                    username : user.email,
                    password : user.password
                }
            });
            const updatedGrievances = await axios.get('http://localhost:8083/grievance/assignee/assignedGrievances', {
                    auth : {
                        username : user.email,
                        password : user.password
                    }
                });
            setGrievances(updatedGrievances.data.data);
            setEditingField(null);
        } catch(error) {
            console.error("Updating status failed.", error);
        }
    };

    return(
        <div className="assignee-grievances-container">
            <LogoutButton />
            <h2 className="h2-assignee">Assigned Grievances</h2>
            {grievances.length? (
                <ul className="assignee-grievance-list">
                    {grievances.map(grievance => (
                        <li key={grievance.grievanceId} className="assignee-grievance-item">
                            <div>
                                <p><strong>Title: </strong>{grievance.title}</p>
                                <p><strong>Description: </strong>{grievance.description}</p>
                                <p><strong>Status: </strong>{grievance.status}</p>                            
                                <p><strong>Submitted On: </strong>{new Date(grievance.submitted_on).toLocaleString()}</p>
                                {grievance.user && (
                                    <div>
                                        <p><strong>User ID: </strong>{grievance.user.userId}</p>
                                        <button onClick={() => toggleUserDetails(grievance.grievanceId)} className="assignee-toggle-details-button">
                                            {showUserDetails[grievance.grievanceId]? 'Hide Details' : 'View Details'}
                                        </button>
                                        {showUserDetails[grievance.grievanceId] && (
                                            <div>
                                                <p><strong>Name: </strong>{grievance.user.firstName} {grievance.user.lastName}</p>
                                                <p><strong>Email: </strong>{grievance.user.email}</p>
                                                <p><strong>Phone: </strong>{grievance.user.phone}</p>
                                            </div>
                                        )}
                                    </div>
                                )}
                                {editingField === grievance.grievanceId ? (
                                    <div className="assignee-assignment-section">
                                        <select value={status} onChange={(e) => setStatus(e.target.value)} className="assignee-status-dropdown">
                                            <option value="PENDING">PENDING</option>
                                            <option value="IN_SOLVING">IN_SOLVING</option>
                                            <option value="RESOLVED">RESOLVED</option>
                                        </select>
                                        <button className="assignee-submit-button" onClick={() => handleStatus(grievance.grievanceId)}>Submit</button>
                                    </div>
                                ) : (
                                    <button className="assignee-update-button" onClick={() => handleEditingField(grievance.grievanceId)}>UPDATE</button>
                                )}
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No new assigned grievances.</p>
            )}
            <AssigneeTab />
        </div>
    )
}

export default AssigneeGrievance;
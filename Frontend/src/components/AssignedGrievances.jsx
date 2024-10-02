import LogoutButton from './LogoutButton.jsx';
import SupervisorTab from './SupervisorTab.jsx';
import AuthContext from '../context/AuthContext.jsx';
import {useState, useContext, useEffect} from 'react';
import axios from 'axios';
import '../styles/AssignedGrievance.css';

const AssignedGrievances = () => {
    const [grievances, setGrievances] = useState([]);
    const {user} = useContext(AuthContext);
    const [showUserDetails, setShowUserDetails] = useState({});
    const [showAssigneeDetails, setShowAssigneeDetails] = useState({});
    const [activeTab, setActiveTab] = useState('allAssigned');

    const fetchGrievances = async (endpoint) => {
        try {
            const response = await axios.get(endpoint, {
                auth: {
                    username: user.email,
                    password: user.password,
                },
            });
            setGrievances(response.data);
        } catch (error) {
            console.error('Fetching grievances failed', error);
        }
    };

    useEffect(() => {
        fetchGrievances('http://localhost:8083/grievance/supervisor/allAssignedGrievances');
    }, [user]);

    const handleTabClick = (tab) => {
        setActiveTab(tab);
        if (tab === 'allAssigned') {
            fetchGrievances('http://localhost:8083/grievance/supervisor/allAssignedGrievances');
        } else if (tab === 'unresolved') {
            fetchGrievances('http://localhost:8083/grievance/supervisor/unresolvedGrievance');
        }
    };

    const toggleUserDetails = (grievanceId) => {
        setShowUserDetails(prevState => ({
            ...prevState,
            [grievanceId] : !prevState[grievanceId]
        }));
    };

    const toggleAssigneeDetails = (grievanceId) => {
        setShowAssigneeDetails(prevState => ({
            ...prevState,
            [grievanceId] : !prevState[grievanceId]
        }));
    };

    return(
        <div className="assigned-grievances-container">
            <LogoutButton />
            <h2 className="h2-assigned-grievances">Status</h2>
            <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '20px' }}>
                <button
                    onClick={() => handleTabClick('allAssigned')}
                    style={{
                        padding: '10px 20px',
                        cursor: 'pointer',
                        backgroundColor: activeTab === 'allAssigned' ? '#87ceeb' : '#fff',
                        color: activeTab === 'allAssigned' ? '#000000' : '#87ceeb',
                        border: '1px solid #000',
                    }}
                >
                    All Assigned Grievances
                </button>
                <button
                    onClick={() => handleTabClick('unresolved')}
                    style={{
                        padding: '10px 20px',
                        cursor: 'pointer',
                        backgroundColor: activeTab === 'unresolved' ? '#87ceeb' : '#fff',
                        color: activeTab === 'unresolved' ? '#000000' : '#87ceeb',
                        border: '1px solid #000',
                    }}
                >
                    Unresolved Grievances
                </button>
            </div>
            {grievances.length? (
                <ul className="assigned-grievance-list ">
                    {grievances.map(grievance => (
                        <li key={grievance.grievanceId} className="assigned-grievance-item ">
                            <div>
                                <p><strong>Title: </strong>{grievance.title}</p>
                                <p><strong>Description: </strong>{grievance.description}</p>
                                <p><strong>Status: </strong>{grievance.status}</p>
                                <p><strong>Category: </strong>{grievance.category }</p>
                                <p><strong>Submitted On: </strong>{new Date(grievance.submitted_on).toLocaleString()}</p>
                                {grievance.user && (
                                    <div>
                                        <p><strong>User ID: </strong>{grievance.user.userId}</p>
                                        <button onClick={() => toggleUserDetails(grievance.grievanceId)} className="assigned-toggle-details-button">
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
                                {grievance.assignee && (
                                    <div>
                                        <p><strong>Assignee ID: </strong>{grievance.assignee.userId}</p>
                                        <button onClick={() => toggleAssigneeDetails(grievance.grievanceId)} className="assigned-toggle-details-button">
                                            {showAssigneeDetails[grievance.grievanceId]? 'Hide Details' : 'View Details'}
                                        </button>
                                        {showAssigneeDetails[grievance.grievanceId] && (
                                            <div>
                                                <p><strong>Name: </strong>{grievance.assignee.firstName} {grievance.assignee.lastName}</p>
                                                <p><strong>Email: </strong>{grievance.assignee.email}</p>
                                                <p><strong>Phone: </strong>{grievance.assignee.phone}</p>
                                            </div>
                                        )}
                                    </div>
                                )}
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No unresolved grievance.</p>
            )}
            <SupervisorTab />
        </div>
    )
}

export default AssignedGrievances;
import AuthContext from '../context/AuthContext.jsx';
import {useState, useContext, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import LogoutButton from './LogoutButton.jsx';
import SupervisorTab from './SupervisorTab.jsx';
import axios from 'axios';
import '../styles/UnassignedGrievances.css';

const UnassignedGrievances = () => {
    const {user} = useContext(AuthContext);
    const [grievances, setGrievances] = useState([]);
    const [category, setCategory] = useState('TECHNICAL');
    const [editingField, setEditingField] = useState(null);
    const [assignee, setAssignee] = useState([]);
    const [selectedAssignee, setSelectedAssignee] = useState('');
    const [showUserDetails, setShowUserDetails] = useState({});

    useEffect(() => {
        const fetchGrievances = async() => {
            try {
                const response = await axios.get('http://localhost:8083/grievance/supervisor/unassignedGrievances', {
                    auth : {
                        username : user.email,
                        password : user.password
                    }
                });
                setGrievances(response.data.data);
            } catch (error) {
                console.error("Fetching failed", error);
            }
        };

        const fetchAssignees = async() => {
            try {
                const response = await axios.get('http://localhost:8083/users/supervisor/allAssignees', {
                    auth : {
                        username : user.email,
                        password : user.password
                    }
                });
                setAssignee(response.data.assignees);
            } catch (error) {
                console.error("Fetching assignees failed.");
            }
        }

        fetchGrievances();
        fetchAssignees();
    }, [user]);

    const handleAssignClick = (grievanceId) => {
        setEditingField(grievanceId);
    }

    const toggleUserDetails = (grievanceId) => {
        setShowUserDetails(prevState => ({
            ...prevState,
            [grievanceId] : !prevState[grievanceId]
        }));
    };

    const handleSubmitAssignment = async (grievanceId) => {
        try {
            const assigneeId = Number(selectedAssignee);
            if (isNaN(assigneeId)) {
                console.error("Invalid assigneeId");
                return;
            }
            const assignment = {grievanceId, assigneeId, category}
            const response = await axios.patch(`http://localhost:8083/grievance/supervisor/assignGrievance`, assignment, {
                auth : {
                    username : user.email,
                    password : user.password
                }
            });
            const updatedGrievances = await axios.get('http://localhost:8083/grievance/supervisor/unassignedGrievances', {
                auth : {
                    username : user.email,
                    password : user.password
                }
            });
            setGrievances(updatedGrievances.data.data);
            setEditingField(null);
        } catch(error) {
            console.error("Assigning grievance failed.",error);
        }
    }

    return (
        <div className="unassigned-grievances-container">
            <LogoutButton />
            <h2 className="h2-unassigned">Unassigned Grievances</h2>
            {grievances.length? (
                <ul className="unassigned-grievance-list">
                    {grievances.map(grievance => (
                        <li key={grievance.grievanceId} className="unassigned-grievance-item">
                            <div>
                                <p><strong>Title: </strong>{grievance.title}</p>
                                <p><strong>Description: </strong>{grievance.description}</p>
                                <p><strong>Status: </strong>{grievance.status}</p>                            
                                <p><strong>Submitted On: </strong>{new Date(grievance.submitted_on).toLocaleString()}</p>
                                {grievance.user && (
                                    <div>
                                        <p><strong>User ID: </strong>{grievance.user.userId}</p>
                                        <button onClick={() => toggleUserDetails(grievance.grievanceId)} className="toggle-details-button">
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
                                    <div className="assignment-section">
                                        <label>
                                            Category: 
                                            <select value={category} onChange={(e) => setCategory(e.target.value)} className="category-dropdown">
                                                <option value="TECHNICAL">TECHNICAL</option>
                                                <option value="FINANCIAL">FINANCIAL</option>
                                            </select>
                                        </label>
                                        <label>
                                            Assignee:
                                            <select value={selectedAssignee} onChange={(e) => setSelectedAssignee(e.target.value)} className="assignee-dropdown">
                                                <option value="">Select assignee</option>
                                                {assignee.map(assigne => (
                                                    <option key={assigne.userId} value={assigne.userId}>
                                                        {assigne.firstName} {assigne.lastName}
                                                    </option>
                                                ))}
                                            </select>
                                        </label>
                                        <button onClick={() => handleSubmitAssignment(grievance.grievanceId)} className="submit-button">Submit</button>
                                    </div>
                                ) : (
                                    <button onClick={() => handleAssignClick(grievance.grievanceId)} className="assign-button">Assign</button>
                                )}
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No unassigned grievances.</p>
            )}
            <SupervisorTab />
        </div>
    )
}

export default UnassignedGrievances;
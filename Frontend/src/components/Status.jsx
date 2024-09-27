import AuthContext from '../context/AuthContext.jsx';
import axios from 'axios';
import {useState, useContext, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import LogoutButton from './LogoutButton.jsx';
import UserTab from './UserTab.jsx';


const Status = () => {
    const { user } = useContext(AuthContext);
    const [grievances, setGrievances] = useState([]);
    const [expandedGrievances, setExpandedGrievances] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        const fetchGrievances = async() => {
            try {
                const response = await axios.get('http://localhost:8083/grievance/user/allGrievances',{
                auth : {
                    username : user.email,
                    password : user.password
                }
            });
            setGrievances(response.data);
            } catch (error) {
                console.error('Error fetching grievnaces', error);
            }
        };
        fetchGrievances();
    }, [user]);

    const toggleDetails = (id) => {
        setExpandedGrievances(prevState => ({
            ...prevState,[id] : !prevState[id]
        }));
    };

    return (
        <div>
            <LogoutButton />
            <h2> Status </h2>
            {grievances.length? (
                <ul>
                    {grievances.map( grievance => (
                        <li key = {grievance.grievanceId}>
                            <div>
                                <strong>{grievance.title}</strong> - {grievance.status}
                                <button onClick={() => toggleDetails(grievance.grievanceId)}>
                                    {expandedGrievances[grievance.grievanceId] ? 'Hide Details' : 'View Details'}
                                </button>
                                {expandedGrievances[grievance.grievanceId] && (
                                    <div style = {{marginTop:'10px', marginLeft:'20px'}}>
                                        <p><strong>Title: </strong>{grievance.title}</p>
                                        <p><strong>Description: </strong>{grievance.description}</p>
                                        <p><strong>Status: </strong>{grievance.status}</p>
                                        <p><strong>Submitted On: </strong>{new Date(grievance.submitted_on).toLocaleString()}</p>
                                    </div>
                                )}
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p> No grievances found</p>
            )}
            <UserTab />
        </div>
    );
};

export default Status;
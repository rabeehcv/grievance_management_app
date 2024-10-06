import { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import AuthContext from '../context/AuthContext.jsx';
import LogoutButton from './LogoutButton.jsx';
import UserTab from './UserTab.jsx';
import '../styles/UserGrievance.css';

const UserGrievance = () => {
    const [title, setTitile] = useState('');
    const [description, setDescription] = useState('');
    const { user } = useContext(AuthContext);
    const [ userDetails, setUserDetails] = useState({});
    const navigate = useNavigate();

    const handleSubmitGrievance = async(e) => {
        e.preventDefault();
        const trimmedTitle = title.trim();
        const trimmedDescription = description.trim();
        if (!trimmedTitle || !trimmedDescription) {
            alert('Title and Description cannot be empty or whitespace!');
            return; 
        }
        const grievance = { title, description };
        try {
            const response = await axios.post('http://localhost:8083/grievance/user/submit', grievance, {
                auth : {
                    username : user.email,
                    password : user.password
                }
            });
            alert('Grievance submitted successfully!');
        } catch (error) {
            console.error("Grievance submission failed.");
        }
    };

    useEffect(() => {
        const fetchUserDetails = async() => {
            try {
                const response = await axios.get('http://localhost:8083/users/accountPage', {
                    auth: {
                        username : user.email,
                        password : user.password
                    }
                });
                setUserDetails(response.data);
            } catch (error) {
                console.error("Fetching user details failed.");
            }
        };
        fetchUserDetails();
    }, [user]);

    return (
        <div className="grievance-container">
            <LogoutButton />
            <h2 className="h2-grievance"> Submit New Grievance</h2>
            <p className="greeting-message">
                Hey {userDetails.firstName}, please let us know if you have any grievances to report.
            </p>
            <form onSubmit = {handleSubmitGrievance} >
                <input type = "text" placeholder = "Title" value = {title} onChange = { (e) => setTitile(e.target.value)} required className="input-field"/>
                <textarea placeholder = "Description" value = {description} onChange = { (e) => setDescription(e.target.value)} required className="textarea-field" />
                <button type = "submit" className="submit-button">Submit</button>
            </form>
            <UserTab />
        </div>
    );
};

export default UserGrievance;
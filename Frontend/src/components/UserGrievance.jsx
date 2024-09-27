import { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import AuthContext from '../context/AuthContext.jsx';
import LogoutButton from './LogoutButton.jsx';
import UserTab from './UserTab.jsx';

const UserGrievance = () => {
    const [title, setTitile] = useState('');
    const [description, setDescription] = useState('');
    const { user } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmitGrievance = async(e) => {
        e.preventDefault();
        const grievance = { title, description };
        try {
            const response = await axios.post('http://localhost:8083/grievance/user/submit', null, {
                params : {
                    title : grievance.title,
                    description : grievance.description
                },
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

    return (
        <div>
            <LogoutButton />
            <h2> Submit New Grievance</h2>
            <form onSubmit = {handleSubmitGrievance}>
                <input type = "text" placeholder = "Title" value = {title} onChange = { (e) => setTitile(e.target.value)} required />
                <textarea placeholder = "Description" value = {description} onChange = { (e) => setDescription(e.target.value)} required />
                <button type = "submit">Submit</button>
            </form>
            <UserTab />
        </div>
    );
};

export default UserGrievance;
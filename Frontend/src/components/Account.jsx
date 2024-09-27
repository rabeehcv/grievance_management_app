import { useNavigate } from 'react-router-dom';
import { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import AuthContext from '../context/AuthContext.jsx';
import LogoutButton from './LogoutButton.jsx';
import UserTab from './UserTab.jsx';
import SupervisorTab from './SupervisorTab.jsx';
import AssigneeTab from './AssigneeTab';

const Account = () => {
    const { user, login } = useContext(AuthContext);
    const [ userDetails, setUserDetails] = useState({});
    const [ editingField, setEditingField] = useState(null);
    const [ newData, setNewData ] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async(field) => {
        let api = '';
        if(field === 'email') {
            api = `http://localhost:8083/users/update/newEmail/${newData}`
        } else if(field === 'password') {
            api = `http://localhost:8083/users/update/newPassword/${newData}`
        } else if(field === 'phone') {
            api = `http://localhost:8083/users/update/newPhone/${newData}`
        }
        try {
            const response = await axios.patch(api, {}, {
                auth : {
                    username : user.email,
                    password : user.password
                }
            });
            setUserDetails(prevState => ({
                ...prevState, 
                [field] : newData
            }));
            if( field === 'email'){
                login({...user, email : newData});
            } else if( field === 'password'){
                login({...user, password : newData});
            }
            setEditingField(null);
        } catch (error) {
            console.error("Failed Updating ${field}")
        }
    };

    const handleEditClick = (field) => {
        setEditingField(field);
        setNewData(userDetails[field]);
    }

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
        <div>
            <LogoutButton />
            <h2>Account Details</h2>
            <div>
                <p><strong>First Name: </strong>{userDetails.firstName}</p>
                <p><strong>Last Name: </strong>{userDetails.lastName}</p>

                <p><strong>Email: </strong>{userDetails.email}</p>
                {editingField === 'email' ? (
                    <div>
                        <input type = "email" placeholder= "New Email" value = {newData} onChange ={(e) => setNewData(e.target.value)} />
                        <button type = "submit" onClick ={() => handleSubmit('email')}>Submit</button>
                    </div>
                ) : (
                    <button onClick={() => handleEditClick('email')}>Edit</button>
                )}

                <p><strong>Password: </strong>{userDetails.password}</p>
                {editingField === 'password' ? (
                    <div>
                        <input type = "password" placeholder= "New Password" value = {newData} onChange ={(e) => setNewData(e.target.value)} />
                        <button type = "submit" onClick ={() => handleSubmit('password')}>Submit</button>
                    </div>
                ) : (
                    <button onClick={() => handleEditClick('password')}>Edit</button>
                )}

                <p><strong>Phone: </strong>{userDetails.phone}</p>
                {editingField === 'phone' ? (
                    <div>
                        <input type = "phone" placeholder= "New phone" value = {newData} onChange ={(e) => setNewData(e.target.value)} />
                        <button type = "submit" onClick ={() => handleSubmit('phone')}>Submit</button>
                    </div>
                ) : (
                    <button onClick={() => handleEditClick('phone')}>Edit</button>
                )}

            </div>
            {user.role === 'USER' ? <UserTab /> : user.role === 'SUPERVISOR' ? <SupervisorTab /> : user.role === 'ASSIGNEE' ? <AssigneeTab /> : null}
        </div>
    )
}

export default Account;
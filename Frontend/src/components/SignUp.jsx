import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/SignUp.css';

const SignUp = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [phone, setPhone] = useState('');
    const navigate = useNavigate();

    const handleSignUp = async(e) => {
        e.preventDefault();
        if (password.length < 8) {
            alert("Password must be at least 8 characters long");
            return; 
        }
        if (!/^\d+$/.test(phone)) {
            alert("Provide a valid phone number");
            return; 
        }
        if (phone.length !=10 ) {
            alert("Phone number must be 10 digits");
            return; 
        }
        const userData = {firstName, lastName, email, password, phone};
        try {
            const response = await axios.post('http://localhost:8083/users/create', userData);
            if (response.data.status === 'Success'){
                alert('User Registration Completed Successfully!');
                navigate('/login');
            } else if (response.data.status === 'Failure') {
                alert('Email already exists. Please use a different email.');
            }
        } catch (error) {
            console.error('Sign Up failed:', error);
        }
    };

    return (
        <div className="signup-container">
            <div className="signup-box">
                <h2>Sign Up</h2>
                <form onSubmit = {handleSignUp}>
                    <input className="signup-input" type = "text" placeholder = "First Name" value = {firstName} onChange = { (e) => setFirstName(e.target.value)} required />
                    <input className="signup-input" type = "text" placeholder = "Last Name" vaue = {lastName} onChange = { (e) => setLastName(e.target.value)} required />
                    <input className="signup-input" type = "email" placeholder = "Email" value = {email} onChange = { (e) => setEmail(e.target.value)} required />
                    <input className="signup-input" type = "password" placeholder = "Password" value = {password} onChange = { (e) => setPassword(e.target.value)} required />
                    <input className="signup-input" type = "tel" placeholder = "Phone" value = {phone} onChange = { (e) => setPhone(e.target.value)} required />
                    <button className="signup-button" type = "submit">Submit</button>
                </form>
                <p>
                    Already have an account? <a href = "/Login">Log In</a>
                </p>
            </div>
        </div>
    );
};

export default SignUp;
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const SignUp = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [phone, setPhone] = useState('');
    const navigate = useNavigate();

    const handleSignUp = async(e) => {
        e.preventDefault();
        const userData = {firstName, lastName, email, password, phone};
        try {
            const response = await axios.post('http://localhost:8083/users/create', userData);
            alert('User Registration Completed Successfully!');
            navigate('/login');
        } catch (error) {
            console.error('Sign Up failed:', error);
        }
    };

    return (
        <div>
            <h2>Sign Up</h2>
            <form onSubmit = {handleSignUp}>
                <input type = "text" placeholder = "First Name" value = {firstName} onChange = { (e) => setFirstName(e.target.value)} required />
                <input type = "text" placeholder = "Last Name" vaue = {lastName} onChange = { (e) => setLastName(e.target.value)} required />
                <input type = "email" placeholder = "Email" value = {email} onChange = { (e) => setEmail(e.target.value)} required />
                <input type = "password" placeholder = "Password" value = {password} onChange = { (e) => setPassword(e.target.value)} required />
                <input type = "tel" placeholder = "Phone" value = {phone} onChange = { (e) => setPhone(e.target.value)} required />
                <button type = "submit">Submit</button>
            </form>
            <p>
                Already have an account? <a href = "/Login">Log In</a>
            </p>
        </div>
    );
};

export default SignUp;
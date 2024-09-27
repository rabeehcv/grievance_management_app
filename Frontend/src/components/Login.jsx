import {useContext, useState} from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import AuthContext from '../context/AuthContext.jsx'

const Login = () => {
    const [role, setRole] = useState('USER')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const {user, login} = useContext(AuthContext)
    const navigate = useNavigate()

    const handleLogin = async (e) => {
        e.preventDefault()
        const credentials = {role, email, password}
        try {
            const response = await axios.get('http://localhost:8083/users/signIn', {
                auth: {
                    username: email,
                    password: password
                }
            });
            login(credentials)
            //navigate('/user/grievance')
            if(credentials.role === 'USER'){
                navigate('/user/grievance')
            } else if(credentials.role === 'SUPERVISOR'){
                navigate('/supervisor/unassignedGrievances')
            } else if(credentials.role === 'ASSIGNEE'){
                navigate('/assignee/assignedGrievances')
            }
            
        } catch (error) {
            alert("Invalid credentials")
            console.error("Login Failed:", error)
        }
    };

    return (
        <div>
            <h2>Login</h2>
            <form onSubmit = {handleLogin}>
                <select value={role} onChange = {(e) => setRole(e.target.value)}>
                    <option value = "USER">USER</option>
                    <option value = "SUPERVISOR">SUPERVISOR</option>
                    <option value = "ASSIGNEE">ASSIGNEE</option>
                </select>
                <input type = "email" placeholder = "Email" value = {email} onChange = {(e) => setEmail(e.target.value)} required />
                <input type = "password" placeholder = "Password" value = {password} onChange = {(e) => setPassword(e.target.value)} required />
                <button type = "submit">Sign In</button>
            </form>
            <p>
                First time user? <a href="/signup">Sign Up</a>
            </p>
        </div>
    );
};
export default Login;
import { Routes, Route } from 'react-router-dom';
import Login from './components/Login.jsx';
import SignUp from './components/SignUp.jsx';
import UserGrievance from './components/UserGrievance.jsx';
import Status from './components/Status.jsx';
import Account from './components/Account.jsx';
import UnassignedGrievances from './components/UnassignedGrievances.jsx';
import AssignedGrievances from './components/AssignedGrievances.jsx';
import AssigneeGrievance from './components/AssigneeGrievance.jsx';

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/user/grievance" element={<UserGrievance />} />
        <Route path="/user/status" element={<Status />} />
        <Route path="/user/account" element={<Account />} />
        <Route path="/supervisor/unassignedGrievances" element={<UnassignedGrievances />}/>
        <Route path="/supervisor/assignedGrievances" element={<AssignedGrievances />}/>
        <Route path="/assignee/assignedGrievances" element={<AssigneeGrievance/>}/>
      </Routes>
    </div>
  );
}

export default App;

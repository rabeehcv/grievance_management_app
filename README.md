# grievance_management_app

## Project Overview
 
The Grievance Management System will consist of three main roles: User, Supervisor, and Assignee. Each role will have specific views and data flows to facilitate the efficient handling of grievances.
 
### User Role:
 
Functionality: Users can raise complaints or queries related to various categories.

View: A user-friendly interface for submitting grievances, viewing status updates.
 
### Supervisor Role:
 
Functionality: Supervisors review submitted grievances, categorize them, and assign them to the appropriate assignees.

View: An interface for managing incoming grievances, filtering by category, and assigning tasks to assignees.
 
### Assignee Role:
 
Functionality: Assignees receive assigned grievances, work on resolving them, and update the status accordingly.

View: A dashboard for viewing assigned grievances, updating statuses, and providing resolution feedback.
 
 
## System Architecture
 
Frontend: React.js will be used to develop a intuitive user interface for all three roles. Each role will have specific components tailored to their functionality.

Backend: Spring Boot and Java will be used to handle the business logic, including grievance submission, categorization, assignment, and resolution tracking.

Database: PostgreSQL will serve as the database for storing user information, grievances, categories, assignments, and status updates.
 
 
## Data Flow
 
### User Submission:
 
Users submit grievances via the React frontend.

The submission is sent to the Spring Boot backend and stored in the PostgreSQL database.
 
 
### Supervisor Assignment:
 
Supervisors access the list of submitted grievances via their interface.
They assign grievances to the appropriate assignees.
The assignments are updated in the PostgreSQL database.
 
 
### Assignee Resolution:
 
Assignees receive notifications of new assignments.
They access the grievances through their dashboard and work on resolving them.
Resolution updates are sent to the backend and reflected in the PostgreSQL database.

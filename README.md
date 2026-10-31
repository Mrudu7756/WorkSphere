# WorkSphere — Enterprise Management Platform

A Java 17, Spring Boot, REST API and MySQL backend for managing employees, projects and tasks.

## Features
- CRUD REST APIs for employees, projects and tasks
- MySQL persistence using Spring Data JPA
- Request validation (`@Valid`, Jakarta Bean Validation)
- HTTP Basic authentication with role-based access:
  - `ADMIN`: read, create, update and delete
  - `MANAGER`: read, create and update
  - `EMPLOYEE`: read-only
- Filter tasks by project or employee
- Basic validation for project dates and task references
- HTTP status codes for common errors

## Requirements
- JDK 17+
- Maven 3.8+
- MySQL 8+

## 1. Create the database (optional)
The JDBC URL is configured with `createDatabaseIfNotExist=true`, but you can create it manually:
```sql
CREATE DATABASE worksphere_db;
```

## 2. Configure MySQL
Open `src/main/resources/application.properties`. The default username is `root` and the password is blank.
Alternatively, set environment variables before running:
- `DB_USERNAME`
- `DB_PASSWORD`

Never commit real production credentials to source control.

## 3. Run the application
From the project root:
```bash
mvn spring-boot:run
```
The API starts at `http://localhost:8080`.

## Demo credentials
Use these only for local development:
| Role | Username | Password |
|---|---|---|
| ADMIN | `admin` | `Admin@123` |
| MANAGER | `manager` | `Manager@123` |
| EMPLOYEE | `employee` | `Employee@123` |

Authentication uses HTTP Basic. In Postman, select **Authorization → Basic Auth** and enter the credentials. Replace the in-memory accounts with a proper user store and secure authentication before production.

## API endpoints

### Employees — `/api/employees`
| Method | Endpoint | Purpose | Access |
|---|---|---|---|
| GET | `/api/employees` | List employees | Any role |
| GET | `/api/employees/{id}` | Get employee | Any role |
| POST | `/api/employees` | Create employee | ADMIN, MANAGER |
| PUT | `/api/employees/{id}` | Update employee | ADMIN, MANAGER |
| DELETE | `/api/employees/{id}` | Delete employee | ADMIN |

Example create employee JSON:
```json
{
  "name": "Aarav Sharma",
  "email": "aarav@example.com",
  "department": "Engineering",
  "jobTitle": "Software Engineer"
}
```

### Projects — `/api/projects`
| Method | Endpoint | Purpose | Access |
|---|---|---|---|
| GET | `/api/projects` | List projects | Any role |
| GET | `/api/projects/{id}` | Get project | Any role |
| POST | `/api/projects` | Create project | ADMIN, MANAGER |
| PUT | `/api/projects/{id}` | Update project | ADMIN, MANAGER |
| DELETE | `/api/projects/{id}` | Delete project | ADMIN |

Example create project JSON:
```json
{
  "name": "Customer Portal",
  "description": "Internal customer support platform",
  "status": "IN_PROGRESS",
  "startDate": "2026-11-01",
  "endDate": "2027-02-28"
}
```
Use ISO dates (`YYYY-MM-DD`). `endDate` is optional and cannot be before `startDate`.

### Tasks — `/api/tasks`
| Method | Endpoint | Purpose | Access |
|---|---|---|---|
| GET | `/api/tasks` | List all tasks | Any role |
| GET | `/api/tasks/{id}` | Get task | Any role |
| GET | `/api/tasks?projectId=1` | Filter by project | Any role |
| GET | `/api/tasks?employeeId=1` | Filter by assignee | Any role |
| POST | `/api/tasks` | Create task | ADMIN, MANAGER |
| PUT | `/api/tasks/{id}` | Update task | ADMIN, MANAGER |
| DELETE | `/api/tasks/{id}` | Delete task | ADMIN |

Example create task JSON (replace IDs with existing records):
```json
{
  "title": "Implement login validation",
  "description": "Add server-side validation for login inputs",
  "status": "TODO",
  "projectId": 1,
  "assigneeEmployeeId": 1,
  "dueDate": "2026-11-15"
}
```

## Test with curl
```bash
curl -u admin:Admin@123 http://localhost:8080/api/employees
```
Create an employee:
```bash
curl -u manager:Manager@123 -H "Content-Type: application/json" \
  -d '{"name":"Aarav Sharma","email":"aarav@example.com","department":"Engineering","jobTitle":"Software Engineer"}' \
  http://localhost:8080/api/employees
```

## MySQL tables
Hibernate creates/updates the `employees`, `projects` and `tasks` tables on startup (`ddl-auto=update`). In MySQL Workbench, refresh the Schemas panel and expand `worksphere_db` → Tables.

## Suggested next improvements
- Replace demo in-memory accounts with database-backed users
- Add JWT authentication, pagination, search and audit logging
- Add automated integration tests and a frontend dashboard
- Add stricter status enums and database foreign-key relationships for projects/tasks

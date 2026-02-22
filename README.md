# Employee Attendance Management System

Full-stack Employee Attendance Management System built with **Java Spring Boot**, **MySQL**, **HTML**, **CSS**, and **Thymeleaf**. Includes secure login, role-based access (Employee/Admin), one-time daily attendance marking, and admin reports.

## Features

- **Secure authentication**: Employee ID + password with Spring Security, BCrypt password encryption, session management
- **Employee**: Mark attendance (Present) once per day; date and time stored automatically
- **Admin**: View all employees, daily attendance report, filter attendance by date
- **MVC architecture**: Controller, Service, Repository, Entity layers
- **Spring Data JPA** for database operations
- **Production-ready**: Configurable server port via `PORT` for deployment (e.g. Render)

## Tech Stack

- Java 17, Spring Boot 3.2
- Spring Security, Spring Data JPA
- MySQL, Thymeleaf, HTML/CSS

## Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8+ (local or hosted)

## Local Setup

1. **Clone and open the project**

2. **Create MySQL database** (optional; app can create it if allowed):
   ```sql
   CREATE DATABASE attendance_db;
   ```

3. **Configure database** (defaults in `application.properties`):
   - `spring.datasource.url` — default: `jdbc:mysql://localhost:3306/attendance_db?...`
   - `spring.datasource.username` — default: `root`
   - `spring.datasource.password` — set as needed

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Open**: http://localhost:8080

### Default accounts (created on first run)

| Role    | Employee ID | Password  |
|---------|-------------|-----------|
| Admin   | `admin`     | `admin123` |
| Employee| `emp001`    | `password` |

## Deploy on Render

1. Create a **MySQL** database on Render and note the **Internal Database URL** (or JDBC URL if provided).

2. Create a **Web Service**, connect your repo, and set:
   - **Runtime**: Java
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -Dserver.port=$PORT -jar target/employee-attendance-system-1.0.0.jar`

3. **Environment variables** (required for production):
   - `PORT` — set automatically by Render
   - `DATABASE_URL` — full JDBC URL, e.g. `jdbc:mysql://host:port/dbname?user=...` (if Render gives a `mysql://` URL, convert it to `jdbc:mysql://...` and add `user` and `password` as query params or use separate variables)
   - `DB_USERNAME` — database user
   - `DB_PASSWORD` — database password  
   Optionally set `SPRING_PROFILES_ACTIVE=prod` to use production settings.

4. If your Render MySQL service only provides a single URL (e.g. `mysql://user:pass@host:port/db`), set:
   - `DATABASE_URL=jdbc:mysql://host:port/db?user=user&password=pass&useSSL=false&serverTimezone=UTC`
   (replace host, port, db, user, pass with values from the Render dashboard.)

## Project Structure

```
src/main/java/com/attendance/
├── AttendanceApplication.java
├── config/          # SecurityConfig, DataInitializer
├── controller/      # Login, Dashboard, Employee, Attendance, Admin
├── entity/          # Employee, Attendance, Role
├── repository/      # JPA repositories
├── security/        # CustomUserDetailsService
└── service/         # EmployeeService, AttendanceService
src/main/resources/
├── application.properties
├── static/css/      # style.css
└── templates/      # login, employee/, admin/
```

## API / Pages

| URL                 | Access   | Description           |
|---------------------|----------|-----------------------|
| `/login`            | Public   | Login page            |
| `/employee/dashboard` | Employee | My dashboard, mark attendance |
| `/attendance/mark`  | Employee | POST – mark present   |
| `/admin/dashboard`  | Admin    | Today’s attendance    |
| `/admin/employees`  | Admin    | All employees         |
| `/admin/attendance` | Admin    | Attendance by date    |

## License

MIT

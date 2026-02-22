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

### Default account (created on first run)

| Role     | User ID (5 digits) | Password  |
|----------|--------------------|-----------|
| In-Charge| `12345`            | `password` |

## Deploy on Render

1. **Go to [Render Dashboard](https://dashboard.render.com)** and sign in (or sign up).

2. **Connect GitHub**: **Account Settings → Connect GitHub** and authorize the repo **arjavjain310/Employee-Attendance-System** (or use **New → Web Service** and select the repo).

3. **Create a MySQL database** (for production):
   - **New → PostgreSQL** is free; for **MySQL** use **New → Private Service** or an external MySQL (e.g. [PlanetScale](https://planetscale.com), [Railway](https://railway.app)).
   - Or skip DB and use **H2 in-memory** for a quick demo: set env `SPRING_PROFILES_ACTIVE=dev` and do **not** set `DATABASE_URL` (data will reset on each deploy).

4. **Create Web Service**:
   - **New → Web Service**
   - Connect repo: **https://github.com/arjavjain310/Employee-Attendance-System**
   - **Runtime**: Java
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -Dserver.port=$PORT -jar target/employee-attendance-system-1.0.0.jar`

5. **Environment variables** (in the Web Service **Environment** tab):
   - `PORT` — auto-set by Render (do not override)
   - For **MySQL**: `DATABASE_URL` (JDBC URL), `DB_USERNAME`, `DB_PASSWORD`, `SPRING_PROFILES_ACTIVE=prod`
   - For **H2 demo**: `SPRING_PROFILES_ACTIVE=dev` only

6. Click **Create Web Service**. Render will build and deploy; your app URL will be like `https://employee-attendance-system-xxxx.onrender.com`.

**Quick deploy (no database):** Set only `SPRING_PROFILES_ACTIVE=dev`. The app will use in-memory H2; data resets on restart. Default login: User ID `12345`, password `password`.

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

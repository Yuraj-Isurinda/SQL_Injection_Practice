# SQL Injection Demo with Spring Boot

This project demonstrates:
1. A **vulnerable** Spring Boot application prone to SQL injection.
2. A **secured** version using `PreparedStatement` and Spring Data JPA.

---

## 🔧 Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+

---

## 🚀 Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/sql-injection-demo.git
   cd sql-injection-demo

Configure MySQL:

Create a database named UserDB.

Update src/main/resources/application.properties with your credentials:

properties
Copy
Edit
spring.datasource.url=jdbc:mysql://localhost:3306/UserDB
spring.datasource.username=root
spring.datasource.password=yourpassword
Run the application:

bash
Copy
Edit
mvn spring-boot:run
🔓 Vulnerable Implementation
❌ Code Snippet (Unsafe)
java
Copy
Edit
// UNSAFE: SQL injection possible
String query = "SELECT * FROM User WHERE username = '" + username + "'";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(query);
🧪 Exploit
Visit: http://localhost:8080

Payload in username field:

sql
Copy
Edit
' OR '1'='1
Result: Dumps all users from the database.

🔐 Secured Implementation
✅ Fix 1: Using PreparedStatement
java
Copy
Edit
// SAFE: Input sanitization
String sql = "SELECT * FROM User WHERE username = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, username);
✅ Fix 2: Using Spring Data JPA (Recommended)
java
Copy
Edit
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUsername(String username);
}
🛡️ Key Security Measures
✅ Parameterized queries (PreparedStatement)

✅ ORM (Spring Data JPA) for automatic escaping

✅ Input validation (e.g., reject ' or --)

✅ Testing
Test Case	Payload	Expected Result
Valid User	admin	Returns matching user
SQL Injection	' OR 1=1 --	"No user found." (Safe)
Malformed Input	'; DROP TABLE User	Rejected (Validation)

📁 Project Structure
bash
Copy
Edit
src/
├── main/
│   ├── java/
│   │   └── com/example/demo/
│   │       ├── controller/UserController.java
│   │       ├── model/User.java
│   │       └── repository/UserRepository.java
│   └── resources/
│       ├── static/        # CSS/JS
│       ├── templates/     # Thymeleaf (index.html)
│       └── application.properties

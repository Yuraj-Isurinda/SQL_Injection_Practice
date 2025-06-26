
# 🛡️ SQL Injection Demo with Spring Boot

This project demonstrates:

1. A **vulnerable** Spring Boot application prone to SQL Injection.
2. A **secured** version using `PreparedStatement` and **Spring Data JPA**.

---

## ⚙️ Configure MySQL

### ✅ Step 1: Create Database

```sql
CREATE DATABASE UserDB;
```

### ✅ Step 2: Configure `application.properties`

File: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/UserDB
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🚀 Run the Application

```bash
mvn spring-boot:run
```

Then visit:  
👉 `http://localhost:8080`

---

## ❌ Vulnerable Implementation (Unsafe)

```java
// UNSAFE: SQL injection possible
String query = "SELECT * FROM User WHERE username = '" + username + "'";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(query);
```

### 🧪 Exploit

Try this in the `username` field:
```sql
' OR '1'='1
```

📌 **Result**: Dumps all users from the database.

---

## ✅ Secured Implementations

### ✅ Fix 1: Use `PreparedStatement`

```java
// SAFE: Prevents SQL injection
String sql = "SELECT * FROM User WHERE username = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, username);
ResultSet rs = pstmt.executeQuery();
```

### ✅ Fix 2: Use Spring Data JPA (Recommended)

```java
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUsername(String username);
}
```

---

## 🛡️ Key Security Measures

- ✅ **Parameterized queries** (`PreparedStatement`)
- ✅ **ORM layer** (Spring Data JPA) – automatic escaping
- ✅ **Input validation** – reject inputs like `'` or `--`

---

## 🧪 Testing

| Payload              | Expected Result                        |
|----------------------|----------------------------------------|
| `admin`              | Returns matching user                  |
| `' OR 1=1 --`        | "No user found." (Safe)                |
| `'; DROP TABLE User` | Rejected (Input validation prevents it)|

---

## 📁 Project Structure

```bash
src/
├── main/
│   ├── java/
│   │   └── com/example/demo/
│   │       ├── controller/
│   │       │   └── UserController.java
│   │       ├── model/
│   │       │   └── User.java
│   │       └── repository/
│   │           └── UserRepository.java
│   └── resources/
│       ├── static/         # CSS/JS
│       ├── templates/      # Thymeleaf (index.html)
│       └── application.properties
```

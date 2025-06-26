package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;

@Controller
public class UserController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/UserDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/search")
    public String searchUser(@RequestParam String username, Model model) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // // UNSAFE: Concatenate user input directly into the query
            // String query = "SELECT * FROM User WHERE username = '" + username + "'";
            // Statement stmt = conn.createStatement();
            // ResultSet rs = stmt.executeQuery(query);

            // Safe: Use PreparedStatement to sanitize input
            String sql = "SELECT * FROM User WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username); // Input is automatically escaped

            ResultSet rs = pstmt.executeQuery();

            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("id")).append("<br>");
                result.append("Username: ").append(rs.getString("username")).append("<br>");
                result.append("Email: ").append(rs.getString("email")).append("<br><br>");
            }

            if (result.length() == 0) {
                model.addAttribute("result", "No user found.");
            } else {
                model.addAttribute("result", result.toString());
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            model.addAttribute("result", "Error: " + e.getMessage());
        }
        return "index";
    }
}

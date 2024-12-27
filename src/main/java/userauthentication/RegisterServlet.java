package userauthentication;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.security.MessageDigest;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        // Hash the password before storing it
        String hashedPassword = hashPassword(password);

        try {
            // Call stored procedure to insert the user
            Connection conn = DBConnection.getConnection();
            String query = "{CALL InsertUser(?, ?, ?, ?, ?)}";
            CallableStatement stmt = conn.prepareCall(query);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, mobile);
            stmt.setString(4, hashedPassword);
            stmt.setString(5, "admin"); // Created by, you can set this dynamically

            boolean result = stmt.execute();
            if (!result) {
                response.sendRedirect("index.html");
            } else {
                response.getWriter().println("Registration failed, please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}

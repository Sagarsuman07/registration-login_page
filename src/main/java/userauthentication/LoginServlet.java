package userauthentication;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.security.MessageDigest;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        // Hash the password
        String hashedPassword = hashPassword(password);

        try {
            // Call stored procedure to check the user
            Connection conn = DBConnection.getConnection();
            String query = "{CALL SelectUser(?)}";
            CallableStatement stmt = conn.prepareCall(query);
            stmt.setString(1, mobile);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getString("password").equals(hashedPassword)) {
                // User authenticated
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                // Set session attributes
                HttpSession session = request.getSession();
                session.setAttribute("firstName", firstName);
                session.setAttribute("lastName", lastName);
                session.setAttribute("greeting", getGreetingMessage());

                // Redirect to landing page
                response.sendRedirect("/userauthentication/landing.jsp");
            } else {
                // Invalid credentials
                response.getWriter().println("Invalid login credentials.");
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

    private String getGreetingMessage() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour < 12) {
            return "Good Morning";
        } else if (hour < 18) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }
}

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Sign_in")
public class Sign_up extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String dob = request.getParameter("dob");
        String agree = request.getParameter("agree");

        // Validate form data
        if (name == null || name.isEmpty() || email == null || email.isEmpty() ||
            password == null || password.isEmpty() || dob == null || dob.isEmpty() || 
            agree == null || agree.isEmpty()) {
            out.println("<script>alert('Please fill in all fields');</script>");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Explicitly load the MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ganesh", "root", "@virat18");

            // Prepare SQL statement
            String sql = "INSERT INTO users (name, email, password, dob) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, dob);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Registration successful
                response.sendRedirect("index.html");
            } else {
                // Registration failed
                out.println("<script>alert('Registration failed. Please try again.');</script>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Log the exception
            e.printStackTrace();
            // Show the exception message as an alert
            out.println("<script>alert('An error occurred: " + e.getMessage() + "');</script>");
        } finally {
            // Close resources using try-with-resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                // Log the exception
                ex.printStackTrace();
            }
        }
    }
}

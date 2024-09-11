import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ganesh";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "@virat18";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieving form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Explicitly load the MySQL JDBC driver class
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Prepare SQL statement
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            // Executing the query
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Login successful
                response.sendRedirect("index.html");
            } else {
                // Login failed
                out.println("<h1>Login Failed. Please check your email and password.</h1>");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Closing resources
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

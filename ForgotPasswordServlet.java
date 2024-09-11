import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@WebServlet("/forgotPassword")
public class ForgotPasswordServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/Password";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "@virat18";
    private static final String EMAIL_FROM = "your_email@example.com"; // Change this to your email address
    private static final String EMAIL_PASSWORD = "your_email_password"; // Change this to your email password

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        // Generate a unique token
        String token = UUID.randomUUID().toString();

        // Store the token in the database along with the user's email
        if (storeTokenInDatabase(email, token)) {
            // Send the password reset email
            sendPasswordResetEmail(email, token);
            response.sendRedirect("passwordResetSuccess.html"); // Redirect to success page
        } else {
            response.sendRedirect("passwordResetError.html"); // Redirect to error page
        }
    }

    private boolean storeTokenInDatabase(String email, String token) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO password_reset_tokens (email, token) VALUES (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, email);
                statement.setString(2, token);
                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendPasswordResetEmail(String email, String token) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.your-email-provider.com"); // Change this to your email provider's SMTP server
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Password Reset");
            message.setText("Click the following link to reset your password: http://yourwebsite.com/resetPassword?token=" + token);
            Transport.send(message);
            System.out.println("Password reset email sent successfully to " + email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

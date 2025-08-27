import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create connection
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bankdb", // replace with your DB
                    "abdulsuban", // your MySQL user
                    "Subbu@2907" // your MySQL password
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}

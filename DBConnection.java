import java.sql.*; 
public class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/bankdb";
        String user = "abdulsuban";          // Change to your MySQL username
        String password = "Subbu@2907";  // Change to your MySQL password

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}

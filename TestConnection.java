
import java.sql.Connection;
public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            System.out.println("✅ Connection Successful!");
            con.close();
        } catch (Exception e) {
            System.out.println("❌ Connection Failed: " + e.getMessage());
        }
    }
}

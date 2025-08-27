public class TestConnection {
    public static void main(String[] args) {
        try {
            java.sql.Connection con = DBConnection.getConnection();
            if (con != null) {
                System.out.println("Connection Successful!");
            } else {
                System.out.println(" Connection is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

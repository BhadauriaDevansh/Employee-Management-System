import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EMSMain {
    private Connection connection;

    public EMSMain() {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connection URL
            String url = "jdbc:sqlserver://localhost:1433;integratedSecurity=true;databaseName=EmployeeManagement;encrypt=true;trustServerCertificate=true;";

            // Establish the connection
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to the database.");

            // Initialize EMSUI instances
            EMSUI employeeUI = new EMSUI(this, this.connection, EMSUI.PanelType.EMPLOYEE);


            // Set frame size and make it visible
            employeeUI.setSize(800, 600);

            // Set frames visible
            employeeUI.setVisible(true);


        } catch (ClassNotFoundException e) {
            System.err.println("Error: SQL Server JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        // Create an instance of EMSMain
        EMSMain emsMain = new EMSMain();
    }
}

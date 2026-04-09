package DataHandling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DataHandler {

    private Connection conn;

    // Azure SQL connection credentials
    private String server = "moha0007-sql-server.database.windows.net";
    private String database = "cs-dsa-4513-sql-db";
    private String username = "YOUR_USERNAME_PLACEHOLDER";
    private String password = "YOUR_PASSWORD_PLACEHOLDER";

    // Resulting connection string
    final private String url =
            String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
                    server, database, username, password);

    // Initialize and save the database connection
    private void getDBConnection() throws SQLException {
        if (conn != null) {
            return;
        }

        this.conn = DriverManager.getConnection(url);
    }

    // Return the result of selecting everything from the product table 
    public ResultSet getAllProducts() throws SQLException {
        getDBConnection();
        
        final String sqlQuery = "SELECT * FROM product;";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);
        return stmt.executeQuery();
    }

    // Inserts an employee into the related employee table with the given attribute values
    public boolean addEmployee(
            int emp_type, String emp_name, float emp_salary, String emp_add, int per_day, String p_type, String education, String position) throws SQLException {

        getDBConnection(); // Prepare the database connection
        int rows_inserted1 = 0;
        int rows_inserted2 = 0;
        int rows_inserted3 = 0;
        
        // Prepare the SQL statement
        	if(emp_type == 1)
        	{
        	try (
            		final PreparedStatement statement1 = conn.prepareStatement("exec q1_enter_worker @name = ?, @salary = ?, @address = ?, @products_per_day = ?;")) {
                        // Populate the query template with the data collected from the user
                        statement1.setString(1, emp_name);
                        statement1.setFloat(2, emp_salary);
                        statement1.setString(3, emp_add);
                        statement1.setInt(4, per_day);
                        
                        // Actually execute the populated query
                        rows_inserted1 = statement1.executeUpdate();
                       
                    }
            }
        	else if(emp_type ==2)
        	{
        		try (
                		final PreparedStatement statement2 = conn.prepareStatement("exec q1_enter_qcontroller @name = ?, @salary = ?, @address = ?, @product_type = ?;")) {
                            // Populate the query template with the data collected from the user
                            statement2.setString(1, emp_name);
                            statement2.setFloat(2, emp_salary);
                            statement2.setString(3, emp_add);
                            statement2.setString(4, p_type);
                            
                            // Actually execute the populated query
                            rows_inserted2 = statement2.executeUpdate();
                        }
        	}
        	else if(emp_type == 3)
        	{
        		try (
                		final PreparedStatement statement3 = conn.prepareStatement("exec q1_enter_tstaff @name = ?, @salary = ?, @address = ?, @education = ?, @position = ?;")) {
                            // Populate the query template with the data collected from the user
                            statement3.setString(1, emp_name);
                            statement3.setFloat(2, emp_salary);
                            statement3.setString(3, emp_add);
                            statement3.setString(4, education);
                            statement3.setString(5, position);
                            
                            // Actually execute the populated query
                            rows_inserted3 = statement3.executeUpdate();
                        }
        	}
                


        // Execute the query, if only one record is updated, then we indicate success by returning true
        	if(rows_inserted1==1 || rows_inserted2==1 || rows_inserted3==1)
        		return true;
        	else
        		return false;
    }
    
    public ResultSet findEmployeeBySalary(float salary) throws SQLException {

        getDBConnection(); // Prepare the database connection
        
        // Prepare the SQL statement
    
        final PreparedStatement statement = conn.prepareStatement("exec q12_salary @salary = ?;");
     
        // Populate the query template with the data collected from the user
        statement.setFloat(1, salary);
                        
        return statement.executeQuery(); //executing the query/stored procedure
                          
    }
    
}
        	
                


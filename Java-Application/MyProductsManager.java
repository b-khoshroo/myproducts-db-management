
// CS/DSA 4513
// Project
// Babak M. Khoshroo

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLWarning;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class project_app {

    // Database credentials
    final static String HOSTNAME = "moha0007-sql-server.database.windows.net";
    final static String DBNAME = "cs-dsa-4513-sql-db";
    final static String USERNAME = "moha0007";
    final static String PASSWORD = "Shailyn.2019";

    // Database connection string
    final static String URL = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
            HOSTNAME, DBNAME, USERNAME, PASSWORD);

    // User input prompt//
    final static String PROMPT =
    		"\n1) Enter a new employee;" + "\n2) Enter a new product;" +
    		"\n3) Enter a customer purchasing a product;" + "\n4) Create a new account;" +
    		"\n5) Enter a complaint;" + "\n6) Enter an accident;" +
    		"\n7) Retrieve the date produced and time spent to produce a particular product;" + 
    		"\n8) Retrieve all products made by a particular worker;" +
    		"\n9) Retrieve the total number of errors a particular quality controller made;" + 
    		"\n10) Retrieve the total costs of the products in the product3 category which were repaired at the request of a particular quality controller;" +
    		"\n11) Retrieve all customers who purchased all products of a particular color;" + 
    		"\n12) Retrieve all employees whose salary is above a particular salary;" +
    		"\n13) Retrieve the total number of work days lost due to accidents in repairing the products which got complaints;" + 
    		"\n14) Retrieve the average cost of all products made in a particular year;" +
    		"\n15) Delete all accidents whose dates are in some range;" +
    		"\n16) Importing new employees from a data file;" +
    		"\n17) Exporting all customers (in name order) who purchased all products of a particular color to a data file;" +
    		"\n18) Quit." +
    		"\n\tPlease enter your option: ";

    public static void main(String[] args) throws SQLException {

        System.out.println("WELCOME TO THE DATABASE SYSTEM OF MyProducts, Inc.\n");

        final Scanner sc = new Scanner(System.in); // Scanner is used to collect the user input
        String option = ""; // Initialize user option selection as nothing
        while (!option.equals("18")) { // As user for options until option 18 is selected
            System.out.print(PROMPT); // Print the available options
            option = sc.next(); // Read in the user option selection

            switch (option) { // Switch between different options
            		
            	case "1": // Insert a new employee
                	//System.out.println("\n\tEntering a new employee:\n");
                    // Collect the new employee data from user
                	System.out.print("\tIs the employee to be inserted a worker, a quality controller, or a technical staff."
                			+ "\n\tEnter 1 for worker, 2 for quality controller, and 3 for tachinal staff:");
                	int employee_type = sc.nextInt();
                	while(employee_type != 1 && employee_type != 2 && employee_type != 3) //checking if the user enters the correct input
                	{
                		System.out.println("\tPlease enter the right number (1, 2, or 3): ");
                		employee_type = sc.nextInt();
                	}
                	
                    System.out.print("\tPlease enter employee name: ");
                    sc.nextLine();
                    final String name = sc.nextLine(); // Read in employee name

                    System.out.print("\tPlease enter employee salary: ");
                    float salary  = sc.nextFloat(); // Read in employee salary

                    System.out.print("\tPlease enter employee address:");
                    sc.nextLine();
                    final String address = sc.nextLine(); // Read in employee address
                    
                    //depending on the employee type, getting the relevant information:
                    int products_per_day = 0;
                    String product_type = "";
                    String education = "";
                    String position = "";
                    if(employee_type == 1)
                    {
                    	System.out.print("\tPlease enter how many products this worker produces every day: ");
                    	products_per_day = sc.nextInt();
                    }
                    else if(employee_type == 2)
                    {
                    	System.out.print("\tPlease enter what product type this quality controller checks"
                    			+ " (enter product1, product2, or product3: ");
                    	product_type = sc.nextLine();
                    }
                    else if(employee_type == 3)
                    {
                    	System.out.print("\tPlease enter education of this technical staff (enter BS, MS, or PhD): ");
                    	education = sc.nextLine();
                    	System.out.print("\tPlease enter the position of this technical staff: ");
                    	position = sc.nextLine();
                    }

                    System.out.print("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                    	if(employee_type==1) //if the employee is a worker: 
                    	{
                    	try (
                        		final PreparedStatement statement1 = connection.prepareStatement("exec q1_enter_worker @name = ?, @salary = ?, @address = ?, @products_per_day = ?;")) {
                                    // Populate the query template with the data collected from the user
                                    statement1.setString(1, name);
                                    statement1.setFloat(2, salary);
                                    statement1.setString(3, address);
                                    statement1.setInt(4, products_per_day);
                                    
                                    // Actually execute the populated query
                                    final int rows_inserted = statement1.executeUpdate();
                                    System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                    SQLWarning warning = statement1.getWarnings();
                                    if(warning != null)
                                    	System.out.println(" !!! " + warning.getMessage());
                                }
                        }
                    	else if(employee_type == 2) //if the employee is a quality controller:
                    	{
                    		try (
                            		final PreparedStatement statement2 = connection.prepareStatement("exec q1_enter_qcontroller @name = ?, @salary = ?, @address = ?, @product_type = ?;")) {
                                        // Populate the query template with the data collected from the user
                                        statement2.setString(1, name);
                                        statement2.setFloat(2, salary);
                                        statement2.setString(3, address);
                                        statement2.setString(4, product_type);
                                        
                                        // Actually execute the populated query
                                        final int rows_inserted = statement2.executeUpdate();
                                        System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                        SQLWarning warning = statement2.getWarnings();
                                        if(warning != null)
                                        	System.out.println(" !!! " + warning.getMessage());
                                    }
                    	}
                    	else if(employee_type == 3) //if the employee is a technical staff:
                    	{
                    		try (
                            		final PreparedStatement statement3 = connection.prepareStatement("exec q1_enter_tstaff @name = ?, @salary = ?, @address = ?, @education = ?, @position = ?;")) {
                                        // Populate the query template with the data collected from the user
                                        statement3.setString(1, name);
                                        statement3.setFloat(2, salary);
                                        statement3.setString(3, address);
                                        statement3.setString(4, education);
                                        statement3.setString(5, position);
                                        
                                        // Actually execute the populated query
                                        final int rows_inserted = statement3.executeUpdate();
                                        System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                        SQLWarning warning = statement3.getWarnings();
                                        if(warning != null)
                                        	System.out.println(" !!! " + warning.getMessage());
                                    }
                    	}
                            
                    }

                    break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                
                case "2": //Query 2: Inserting a product
                	sc.nextLine();
                	System.out.print("Please enter the following pieces of information about the product:\n\n"
                	+ "\tType of the product (product1, product2, or product3): ");
                	product_type = sc.nextLine(); //Reading the product type
                	System.out.print("\tProduct ID: ");
                	String product_id = sc.nextLine(); //Reading product ID
                	System.out.print("\tDate of production (YYYY-MM-DD): ");
                	String date_produced = sc.nextLine();
                	System.out.print("\tSize (small, medium, or large): ");
                	String size = sc.nextLine();
                	System.out.print("\tTime spent to produce the product: ");
                	float time_spent = sc.nextFloat();
                	System.out.print("\tWorker who produced it: ");
                	sc.nextLine();
                	String worker = sc.nextLine();
                	System.out.print("\tQuality controller: ");
                	String q_controller = sc.nextLine();
                	System.out.print("\tRepair person if the product has been repaired: ");
                	String repair_person = sc.nextLine();
                	if(repair_person == "")
                		repair_person = null;
                	
                	String software_name = "";
                	if(product_type.equals("product1")) //getting software name if it is product1
                	{
                		System.out.print("\tSoftware used in production: ");
                		software_name = sc.nextLine();
                	}
                	
                	String color = "";
                	if(product_type.equals("product2")) //getting color if it is porduct2
                	{
                		System.out.print("\tColor: ");
                		color = sc.nextLine();
                	}
                	
                	float weight = 0;
                	if(product_type.equals("product3")) //getting weight if it is porduct3
                	{
                		System.out.print("\tWeight: ");
                		weight = sc.nextFloat();
                	}
                	
                	System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                    	try (final PreparedStatement statement = connection.prepareStatement("exec q2_enter_product @product_type = ?, @id = ?, @date_produced = ?, "
                        				+ "@time_spent = ?, @worker = ?, @tester = ?, @repair_person = ?, @size = ?, "
                        				+ "@software_name = ?, @color = ?, @weight = ?;")) {
                              // Populate the query template with the data collected from the user
                              statement.setString(1, product_type);
                              statement.setString(2, product_id);
                              statement.setString(3, date_produced);
                              statement.setFloat(4, time_spent);
                              statement.setString(5, worker);
                              statement.setString(6, q_controller);
                              statement.setString(7, repair_person);
                              statement.setString(8, size);
                              //checking the type of the product for setting the inputs of the stored procedure:
                              if(product_type.equals("product1"))
                              {
                                   statement.setString(9, software_name);
                                   statement.setString(10, null);
                                   statement.setString(11, null);
                                    	
                               }
                               else if(product_type.equals("product2"))
                               {
                                   statement.setString(9, null);
                                   statement.setString(10, color);
                                   statement.setString(11, null);
                                    	
                               }
                               else if(product_type.equals("product3"))
                               {
                                   statement.setString(9, null);
                                   statement.setString(10, null);
                                   statement.setFloat(11, weight);
                                    	
                               }
                                    
                               // Actually execute the populated query
                               final int rows_inserted = statement.executeUpdate();
                               System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                               SQLWarning warning = statement.getWarnings();
                               if(warning != null)
                                   System.out.println(" !!! " + warning.getMessage());
                           }
                    	}
                
                	
                    	break;
                
              //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                
                case "3": //inserting customer
                	sc.nextLine(); //consuming the new line
                	System.out.print("\tCustomer name: ");
                	String cus_name = sc.nextLine();
                	System.out.print("\tCustomer address: ");
                	String cus_add = sc.nextLine();
                	System.out.print("\tThe ID of the product purchased: ");
                	product_id = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                    	try (final PreparedStatement statement = connection.prepareStatement("exec q3_insert_customer @cus_name = ?, @cus_add = ?, @product_id = ?;")) {
                              // Populate the query template with the data collected from the user
                              statement.setString(1, cus_name);
                              statement.setString(2, cus_add);
                              statement.setString(3, product_id);
                                    
                               // Actually execute the populated query
                               final int rows_inserted = statement.executeUpdate();
                               System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                               SQLWarning warning = statement.getWarnings();
                               if(warning != null)
                                   System.out.println(" !!! " + warning.getMessage());
                    	}
                    	
                	}
                	
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                	
                case "4": //inserting an account
                	sc.nextLine();
                	System.out.print("\tAccount number: ");
                	String acc_num = sc.nextLine();
                	System.out.print("\tDate: ");
                	String acc_date = sc.nextLine();
                	System.out.print("\tCost: ");
                	float cost = sc.nextFloat();
                	sc.nextLine();
                	System.out.print("\tProduct ID: ");
                	product_id = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                    	try (final PreparedStatement statement = connection.prepareStatement("exec q4_insert_account @acc_num = ?, @date = ?, @cost = ?, @p_id = ?;")) {
                                    // Populate the query template with the data collected from the user
                                    statement.setString(1, acc_num);
                                    statement.setString(2, acc_date);
                                    statement.setFloat(3, cost);
                                    statement.setString(4, product_id);
                                    
                                 // Actually execute the populated query
                                    final int rows_inserted = statement.executeUpdate();
                                    System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                    SQLWarning warning = statement.getWarnings();
                                    if(warning != null)
                                    	System.out.println(" !!! " + warning.getMessage());
                    	}
                    	
                	}
                	
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                	
                case "5": //inserting a customer complaint
                	sc.nextLine();
                	System.out.print("\tComplaint ID: ");
                	String complaint_id = sc.nextLine();
                	System.out.print("\tCustomer Name: ");
                	cus_name = sc.nextLine();
                	System.out.print("\tProduct ID: ");
                	product_id = sc.nextLine();
                	System.out.print("\tComplaint Date: ");
                	String comp_date = sc.nextLine();
                	System.out.print("\tComplaint Description: ");
                	String description = sc.nextLine();
                	System.out.print("\tTreatment (Enter 1 for 'Refund' or 2 for 'Exchange'): ");
                	int treatment_option = sc.nextInt();
                	sc.nextLine();
                	String treatment = "";
                	if(treatment_option == 1)
                		treatment = "Refund";
                	else
                		treatment = "Exchange";
                	
                	//Making sql query and performing it:
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                    	try (final PreparedStatement statement = connection.prepareStatement("exec q5_insert_complaint @id = ?, @cus_name = ?, @p_id = ?, "
                    			+ "@date = ?, @description = ?, @treatment = ?;")) {
                                    // Populate the query template with the data collected from the user
                                    statement.setString(1, complaint_id);
                                    statement.setString(2, cus_name);
                                    statement.setString(3, product_id);
                                    statement.setString(4, comp_date);
                                    statement.setString(5, description);
                                    statement.setString(6, treatment);
                                    
                                 // Actually execute the populated query
                                    final int rows_inserted = statement.executeUpdate();
                                    System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                    SQLWarning warning = statement.getWarnings();
                                    if(warning != null)
                                    	System.out.println(" !!! " + warning.getMessage());
                    	}
                    	
                	}
                	
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                	
                case "6": //Insert accident
                	sc.nextLine();
                	System.out.print("\tAccident Number: ");
                	String accident_number = sc.nextLine();
                	System.out.print("\tAccident Date: ");
                	String accident_date = sc.nextLine();
                	System.out.print("\tDays lost due to the accident: ");
                	int d_lost = sc.nextInt();
                	System.out.print("\tProduct ID: ");
                	sc.nextLine(); //consuming next line
                	product_id = sc.nextLine();
                	System.out.println("\tTo which is it related: a production or repair? Enter the relevant employee name below and leave the non-relevant one empty: ");
                	System.out.print("\t\tWorker: ");
                	worker = sc.nextLine();
                	if(worker=="")
                		worker = null;
                	System.out.print("\t\tTechnical staff: ");
                	repair_person = sc.nextLine();
                	if(repair_person=="")
                		repair_person = null;
                	
                	//Making sql query and performing it:
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                    	try (final PreparedStatement statement = connection.prepareStatement("exec q6_insert_accident @number = ?, @date = ?, @d_lost = ?, "
                    			+ "@p_id = ?, @worker = ?, @repair_person = ?;")) {
                                    // Populate the query template with the data collected from the user
                                    statement.setString(1, accident_number);
                                    statement.setString(2, accident_date);
                                    statement.setInt(3, d_lost);
                                    statement.setString(4, product_id);
                                    statement.setString(5, worker);
                                    statement.setString(6, repair_person);
                                    
                                 // Actually execute the populated query
                                    final int rows_inserted = statement.executeUpdate();
                                    System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                    SQLWarning warning = statement.getWarnings();
                                    if(warning != null)
                                    	System.out.println(" !!! " + warning.getMessage());
                    	}
                    	
                	}
                	
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                	
                case "7": //query about a product
                	sc.nextLine();
                	System.out.print("\tProduct ID: ");
                	product_id = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q7_date_time @product_id=?;")) {

                                statement.setString(1, product_id);
                                
                                ResultSet resultSet = statement.executeQuery(); //executing the query/stored procedure
                                System.out.println("\n\tRESULT:");
                                if(resultSet.next() == false)
                                	System.out.println("\t\tNo product was found.");
                                else
                                {
                                	System.out.println("\t\tDate of production: " + resultSet.getString(1));
                                	System.out.println("\t\tTime spent: " + resultSet.getString(2));
                                }
                               
                        } 
                    }  

                    break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    
                case "8": //find a product based on a worker
                	
                	sc.nextLine();
                	System.out.print("\tWorker Name: ");
                	worker = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q8_products_worker @worker=?;")) {

                                statement.setString(1, worker);
                                
                                ResultSet resultSet = statement.executeQuery(); //executing the query/stored procedure
                                System.out.println("\n\tRESULT:");
                                if(resultSet.next() == false)
                                	System.out.println("\t\tNo product related to this worker was found.");
                                else
                                {
                                	System.out.print("\t\tProduct ID's related to this worker: ");
                                	do
                                	{
                                		System.out.print(resultSet.getString(1) + "  ");
                                	} while(resultSet.next());
                                	System.out.println();
                                }
                               
                        } 
                    }  

                    break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    
                case "9": //finding the number of errors by a specific quality controller (tester)
                	sc.nextLine();
                	System.out.print("\tQuality controller name: ");
                	q_controller = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q9_errors @tester=?;")) {

                        	statement.setString(1, q_controller);
                        	Boolean result = statement.execute();
                        	if(!result)
                        	{
                        		SQLWarning warning = statement.getWarnings();
                        		System.out.println(" !!! " + warning.getMessage());
                        	}
                        		
                        	else
                        	{
                        		ResultSet resultSet = statement.executeQuery();
                        		resultSet.next();
                        		System.out.println("\n\tRESULT:" + "This quality controller has made " + resultSet.getInt("errors") + " error(s).");
                        	}
                        } 
                    }  

                    break;
               
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    
                case "10": //query about the total cost in product3 category
                	sc.nextLine();
                	System.out.print("\tQuality controller name: ");
                	q_controller = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q10_total_cost @tester=?;")) {

                        	statement.setString(1, q_controller);
                        	Boolean result = statement.execute();
                        	if(!result)
                        	{
                        		SQLWarning warning = statement.getWarnings();
                        		System.out.println(" !!! " + warning.getMessage());
                        	}
                        		
                        	else
                        	{
                        		ResultSet resultSet = statement.executeQuery();
                        		resultSet.next();
                        		System.out.println("\n\tRESULT:" + "The total cost is: " + resultSet.getString(1));
                        	}
                        } 
                    }  
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                	
                case "11": //query for finding the customer purchasing a specific color 
                	sc.nextLine();
                	System.out.print("\tColor: ");
                	color = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q11_color @color=?;")) {

                        	statement.setString(1, color);
                        	ResultSet resultSet = statement.executeQuery(); //executing the query/stored procedure
                            System.out.println("\n\tRESULT:");
                            if(resultSet.next() == false)
                            	System.out.println("\t\tNo such a customer was found.");
                            else
                            {
                            	System.out.print("\t\tFound customers: ");
                            	do
                            	{
                            		System.out.print(resultSet.getString(1) + "  ");
                            	} while(resultSet.next());
                            	System.out.println();
                            }
                        } 
                    }  
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                
                case "12": //finding salaries above a specific amount 
                	sc.nextLine();
                	System.out.print("\tSalary: ");
                	salary = sc.nextFloat();
                	sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q12_salary @salary=?;")) {

                        	statement.setFloat(1, salary);
                        	ResultSet resultSet = statement.executeQuery(); //executing the query/stored procedure
                            System.out.println("\n\tRESULT:");
                            if(resultSet.next() == false)
                            	System.out.println("\t\tNo such an employee was found.");
                            else
                            {
                            	System.out.print("\t\tFound employees: ");
                            	do
                            	{
                            		System.out.print(resultSet.getString(1) + "  ");
                            	} while(resultSet.next());
                            	System.out.println();
                            }
                        } 
                    }  
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^	
                	
                case "13": //finding the number of days lost due to an accident
                	sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q13_days_lost;")) {

                        	ResultSet resultSet = statement.executeQuery();
                        	resultSet.next();
                        	if(resultSet.getString(1)==null)
                        		System.out.println("\tRESULT: No such an accident was found.");
                        	else
                        		System.out.println("\n\tRESULT:" + "The total days lost: " + resultSet.getString(1));
                        } 
                    }  
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^	
                	
                case "14": //the average cost of all products made in a particular year
                	sc.nextLine();
                	System.out.print("\tYear: ");
                	String year = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q14_avg_cost @year=?;")) {

                        	statement.setString(1, year);
                        	ResultSet resultSet = statement.executeQuery();
                        	resultSet.next();
                        	if(resultSet.getString(1)==null)
                        		System.out.println("\n\tRESULT: No such a product was found.");
                        	else
                        		System.out.println("\n\tRESULT: " + resultSet.getString(1));
                        } 
                    }  
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                	
                case "15": //deleting accidents based on a date range 
                	sc.nextLine();
                	System.out.print("\tFrom: ");
                	String beginning = sc.nextLine();
                	System.out.print("\tTo: ");
                	String end = sc.nextLine();
                	
                	try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement("exec q15_accident_date @beginning=?, @end=?;")) {

                        	statement.setString(1, beginning);
                        	statement.setString(2, end);
                        	final int rows_deleted = statement.executeUpdate();
                        	
                        	SQLWarning warning = statement.getWarnings();
                            if(warning != null)
                            	System.out.println(" \n!!! " + warning.getMessage());
                            else
                            	System.out.println(String.format("Done. %d rows deleted.", rows_deleted));
                        	
                        } 
                    }  
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                	
                case "16": //Importing employee information from a text file and inserting them into the DB
                	
                	sc.nextLine();
                	System.out.println("\tFrom which file do you want to import the employee information? Enter the file name or path: ");
                	String fileName = sc.nextLine();
                	
            		try
            		{
            			File myFile = new File(fileName);
            			Scanner fileReader = new Scanner(myFile);
            			while(fileReader.hasNextLine())
            			{
            				String line = fileReader.nextLine();
            				
            				Scanner lineReader = new Scanner(line);
            				String emp_type = lineReader.next(); //reading employee type (worker, quality_controller, or technical_staff)
            				String emp_name = lineReader.next(); //reading employee name
            				float emp_salary = lineReader.nextFloat(); //reading salary
            				String emp_add = lineReader.next(); //reading address
            				int emp_per_day = 0; 
            				String p_type = "";
            				String emp_edu = "";
            				String emp_pos = "";
            				if(emp_type.equals("worker")) //reading product_per_day if worker
            					emp_per_day = lineReader.nextInt();
            					
            				if(emp_type.equals( "quality_controller")) //reading product_type if quality_controller
            					p_type = lineReader.next();
            				if(emp_type.equals("technical_staff")) //reading education and position if technical staff
            				{
            					emp_edu = lineReader.next();
            					emp_pos = lineReader.next();
            				}
            					
            				lineReader.close();
            				
            				System.out.println("Connecting to the database...");
                            // Get a database connection and prepare a query statement
                            try (final Connection connection = DriverManager.getConnection(URL)) {
                            	if(emp_type.equals("worker"))
                            	{
                            	try (
                                		final PreparedStatement statement1 = connection.prepareStatement("exec q1_enter_worker @name = ?, @salary = ?, @address = ?, @products_per_day = ?;")) {
                                            // Populate the query template with the data collected from the user
                                            statement1.setString(1, emp_name);
                                            statement1.setFloat(2, emp_salary);
                                            statement1.setString(3, emp_add);
                                            statement1.setInt(4, emp_per_day);
                                            
                                            // Actually execute the populated query
                                            final int rows_inserted = statement1.executeUpdate();
                                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                            SQLWarning warning = statement1.getWarnings();
                                            if(warning != null)
                                            	System.out.println(" !!! " + warning.getMessage());
                                        }
                                }
                            	else if(emp_type.equals("quality_controller"))
                            	{
                            		try (
                                    		final PreparedStatement statement2 = connection.prepareStatement("exec q1_enter_qcontroller @name = ?, @salary = ?, @address = ?, @product_type = ?;")) {
                                                // Populate the query template with the data collected from the user
                                                statement2.setString(1, emp_name);
                                                statement2.setFloat(2, emp_salary);
                                                statement2.setString(3, emp_add);
                                                statement2.setString(4, p_type);
                                                
                                                // Actually execute the populated query
                                                final int rows_inserted = statement2.executeUpdate();
                                                System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                                SQLWarning warning = statement2.getWarnings();
                                                if(warning != null)
                                                	System.out.println(" !!! " + warning.getMessage());
                                            }
                            	}
                            	else if(emp_type.equals("technical_staff"))
                            	{
                            		try (
                                    		final PreparedStatement statement3 = connection.prepareStatement("exec q1_enter_tstaff @name = ?, @salary = ?, @address = ?, @education = ?, @position = ?;")) {
                                                // Populate the query template with the data collected from the user
                                                statement3.setString(1, emp_name);
                                                statement3.setFloat(2, emp_salary);
                                                statement3.setString(3, emp_add);
                                                statement3.setString(4, emp_edu);
                                                statement3.setString(5, emp_pos);
                                                
                                                // Actually execute the populated query
                                                final int rows_inserted = statement3.executeUpdate();
                                                System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                                SQLWarning warning = statement3.getWarnings();
                                                if(warning != null)
                                                	System.out.println(" !!! " + warning.getMessage());
                                            }
                            	}
                                    
                            }// end of inserting into the database

            			}//end of while loop for reading the file line by line
            			
            			fileReader.close();
            		}
            		catch(FileNotFoundException e)
            		{
            			System.out.println("Error: ");
            			e.printStackTrace();
            		}
            		
            		break;
            	
            	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^	
            		
                case "17": //Exporting to the file all customers who purchased all products of a particular color 
            		sc.nextLine();
            		System.out.println("\tTo which file do you want to export the result? Enter the file name or path: ");
            		String exportFileName = sc.nextLine();
            		System.out.print("\twhat is your intended color: ");
            		String color_choice = sc.nextLine();
            		try
            		{
            			FileWriter fileWriter = new FileWriter(exportFileName);
            			//fileWriter.write("Excellent!");
            			//fileWriter.close();
            		
            			try (final Connection connection = DriverManager.getConnection(URL)) {
            				try (final PreparedStatement statement = connection.prepareStatement("exec q11_color @color=?;")) {

            					statement.setString(1, color_choice);
            					ResultSet resultSet = statement.executeQuery(); //executing the query/stored procedure
            					System.out.println("\n\tRESULT:");
            					if(resultSet.next() == false)
            					{
            						System.out.println("\t\tNo such a customer was found (reported also in the file).");
            						fileWriter.write("No customer was found who purchased all products of " + color_choice + " color.");
            					}		
            					else
            					{
            						System.out.println("\t\tFound customers are being written in the file... ");
            						fileWriter.write("Customers found who purchased all products of " + color_choice + " color:\n\n");
            						do
            						{
            							fileWriter.write("\t" + resultSet.getString(1) + " \n");
            						} while(resultSet.next());
            						System.out.println("\t\tDONE!");
            					}
            					fileWriter.close();
            				} 
            			}// end of connection to the DB
            		}
    				catch(IOException e)
    				{
    					System.out.println("! File writing error: ");
    					e.printStackTrace();
    				}
            		break;

            	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            		
                case "18":
                	System.out.println("\n\t*** END OF THE PROGRAM ***");
                	break;
                
                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                
                default: // Unrecognized option, re-prompt the user for the correct one
                    System.out.println(String.format(
                        "Unrecognized option: %s\n" + 
                        "Please try again!", 
                        option));
                    break;
            }
        }

        sc.close(); // Closing the scanner
    }
}

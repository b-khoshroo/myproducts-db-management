<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Query Result</title>
</head>
    <body style="background-color:yellow;">
    <%@page import="DataHandling.DataHandler"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="java.sql.Array"%>
    <%
    // The handler is the one in charge of establishing the connection.
    DataHandler handler = new DataHandler();

    // Get the attribute values passed from the input form.
    String salaryString = request.getParameter("salary");

    /*
     * Checking if the user has given any amount:
     */
    if (salaryString.equals("")) {
        response.sendRedirect("q12_employee_salary_form.jsp");
    } else {
        float salary = Float.parseFloat(salaryString); 
        
        // Now perform the query with the data from the form.
        final ResultSet resultSet = handler.findEmployeeBySalary(salary);
        if (resultSet.next() == false) { // no employee was found
            %>
                <h2>No such an employee was found.</h2>
            <%
        } else { // showin the result to the user
            %>
            <h2>The employees found:</h2>

            	<%
            	int i = 1;
            	do
            	{
            	%>
                	<blockquote><%=i%>: <%=resultSet.getString(1)%></blockquote>
            	<%
            	i++;
        		}while(resultSet.next());
    	}
    }
    	%>
    </body>
</html>

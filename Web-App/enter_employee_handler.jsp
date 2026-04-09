<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Query Result</title>
</head>
    <body>
    <%@page import="DataHandling.DataHandler"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="java.sql.Array"%>
    <%
    // The handler is the one in charge of establishing the connection.
    DataHandler handler = new DataHandler();

    // Get the attribute values passed from the input form.
    String emp_typeString = request.getParameter("emp_type");
    String emp_name = request.getParameter("emp_name");
    String emp_salaryString = request.getParameter("emp_salary");
    String emp_add = request.getParameter("emp_add");
    String per_dayString = request.getParameter("per_day");
    String p_type = request.getParameter("p_type");
    String education = request.getParameter("education");
    String position = request.getParameter("position");

    /*
     * If the user hasn't filled out all the time, movie name and duration. This is very simple checking.
     */
    if (emp_typeString.equals("") || emp_name.equals("") || emp_salaryString.equals("") || emp_add.equals("")) {
        response.sendRedirect("q2_enter_employee_form.jsp");
    } else {
        float emp_salary = Float.parseFloat(emp_salaryString); 
        int emp_type = Integer.parseInt(emp_typeString);
        int per_day = 0;
        if(emp_type==1)
        	 per_day = Integer.parseInt(per_dayString);
        
        // Now perform the query with the data from the form.
        boolean success = handler.addEmployee(emp_type, emp_name, emp_salary, emp_add, per_day, p_type, education, position);
        if (!success) { // Something went wrong
            %>
                <h2>There was a problem inserting the employee</h2>
            <%
        } else { // Confirm success to the user
            %>
            <h2>The Employee:</h2>

            <ul>
                <li>Name: <%=emp_name%></li>
                <li>Address: <%=emp_add%></li>
                <li>Salary: <%=emp_salaryString%></li>
                <%if(emp_type == 1) {//if a worker %>
                <li>Products per day: <%=per_dayString%></li>
                <%}%>
                <%if(emp_type == 2){//if a quality controller %>
                <li>Checking product type: <%=p_type%></li>
                <%} %>
                <%if(emp_type == 3){ //if a technical staff %>
                <li>Education: <%=education%></li>
                <li>Position: <%=position%></li>
                <%}%>
            </ul>

            <h2>Was successfully inserted.</h2>
            
            <%
        }
    }
    %>
    </body>
</html>

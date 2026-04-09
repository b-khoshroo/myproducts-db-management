<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add Employee</title>
    </head>
    <body>
        <h2>Add Employee</h2>
        <!--
            Form for collecting user input for the new movie_night record.
            Upon form submission, add_movie.jsp file will be invoked.
        -->
        <form action="q2_enter_employee.jsp">
            <!-- The form organized in an HTML table for better clarity. -->
            <table border=1>
                <tr>
                    <th colspan="2">Enter the Employee Data: (Leave the field blank if it is not relevant to the type of employee you want to add)</th>
                </tr>
                <tr>
                    <td>Enter 1 for worker, 2 for Quality Controller, or 3 for Repair Person:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=emp_type>
                    </div></td>
                </tr>
                <tr>
                    <td>Employee Name:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=emp_name>
                    </div></td>
                </tr>
                <tr>
                    <td>Salary:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=emp_salary>
                    </div></td>
                </tr>
                <tr>
                    <td>Employee Address:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=emp_add>
                    </div></td>
                </tr>
                <tr>
                    <td>Products Per Day (If Worker):</td>
                    <td><div style="text-align: center;">
                    <input type=text name=per_day>
                    </div></td>
                </tr>
                <tr>
                    <td>Product Type (If Quality Controller):</td>
                    <td><div style="text-align: center;">
                    <input type=text name=p_type>
                    </div></td>
                </tr>
                <tr>
                    <td>Education (If Technical Staff):</td>
                    <td><div style="text-align: center;">
                    <input type=text name=education>
                    </div></td>
                </tr>
                <tr>
                    <td>Position (If Technical Staff):</td>
                    <td><div style="text-align: center;">
                    <input type=text name=position>
                    </div></td>
                </tr>
                <tr>
                    <td><div style="text-align: center;">
                    <input type=reset value=Clear>
                    </div></td>
                    <td><div style="text-align: center;">
                    <input type=submit value=Insert>
                    </div></td>
                </tr>
            </table>
        </form>
    </body>
</html>

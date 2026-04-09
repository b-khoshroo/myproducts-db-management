<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Find Employees</title>
    </head>
    <body style="background-color:yellow;">
        <h2>Find Employees</h2>
        <!--
            Form for collecting user input for the new movie_night record.
            Upon form submission, add_movie.jsp file will be invoked.
        -->
        <form action="q12_employee_salary.jsp">
            <!-- The form organized in an HTML table for better clarity. -->
            <table border=1>
                <tr>
                    <th colspan="2">Finding All Employees Having Salaries above a Particular Amount</th>
                </tr>
                <tr>
                    <td>Enter your intended salary amount:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=salary>
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

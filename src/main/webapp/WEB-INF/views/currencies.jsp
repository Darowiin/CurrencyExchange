<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Currencies</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
    <h1>Available Currencies</h1>

    <table>
        <tr>
            <th>ID</th>
            <th>Code</th>
            <th>Full Name</th>
            <th>Sign</th>
        </tr>
        <c:forEach items="${currencies}" var="currency">
            <tr>
                <td>${currency.id}</td>
                <td>${currency.code}</td>
                <td>${currency.fullName}</td>
                <td>${currency.sign}</td>
            </tr>
        </c:forEach>
    </table>

    <p><a href="../../index.jsp">Back to Home</a></p>
</body>
</html>


<%@ page contentType="text/html;charset=UTF-8" %>
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
        .form-container {
            margin-top: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            background-color: #f9f9f9;
        }
        .form-group {
            margin-bottom: 10px;
        }
        label {
            display: inline-block;
            width: 120px;
        }
        input[type="text"] {
            padding: 5px;
            width: 200px;
        }
        input[type="submit"] {
            padding: 8px 15px;
            margin-top: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
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

    <div class="form-container">
        <h2>Add New Currency</h2>
        <form action="${pageContext.request.contextPath}/currencies" method="POST">
            <div class="form-group">
                <label for="currencyCode">Code:</label>
                <input type="text" id="currencyCode" name="currencyCode" required placeholder="e.g. USD">
            </div>
            <div class="form-group">
                <label for="currencyName">Full name:</label>
                <input type="text" id="currencyName" name="currencyName" required placeholder="e.g. US Dollar">
            </div>
            <div class="form-group">
                <label for="currencySign">Sign:</label>
                <input type="text" id="currencySign" name="currencySign" required placeholder="e.g. $">
            </div>
            <input type="submit" value="Add Currency">
        </form>
    </div>

    <p><a href="../../index.jsp">Back to Home</a></p>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: amick Khada
  Date: 5/2/2026
  Time: 9:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - InvenTrack POS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card auth-card-wide">
        <div class="auth-header">
            <h1>Create Account</h1>
            <p>Register your staff account for InvenTrack</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                    ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="POST">
            <div class="form-group">
                <label for="fullName" class="form-label">Full Name</label>
                <input type="text" id="fullName" name="fullName" class="form-control" value="${param.fullName}" required>
            </div>

            <div class="form-group">
                <label for="username" class="form-label">Username</label>
                <input type="text" id="username" name="username" class="form-control" value="${param.username}" required>
            </div>

            <div class="form-group">
                <label for="email" class="form-label">Email</label>
                <input type="email" id="email" name="email" class="form-control" value="${param.email}">
            </div>

            <div class="form-group">
                <label for="phone" class="form-label">Phone</label>
                <input type="text" id="phone" name="phone" class="form-control" value="${param.phone}">
            </div>

            <div class="form-group">
                <label for="roleId" class="form-label">Role</label>
                <select id="roleId" name="roleId" class="form-control" required>
                    <option value="">Select role</option>
                    <option value="1" ${param.roleId == '1' ? 'selected' : ''}>Admin</option>
                    <option value="2" ${param.roleId == '2' ? 'selected' : ''}>Cashier</option>
                    <option value="3" ${param.roleId == '3' ? 'selected' : ''}>Stock Manager</option>
                </select>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="confirmPassword" class="form-label">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
            </div>

            <button type="submit" class="btn btn-primary">Register</button>
        </form>

        <p class="auth-link">
            Already have an account?
            <a href="${pageContext.request.contextPath}/login">Sign in</a>
        </p>
    </div>
</div>
</body>
</html>


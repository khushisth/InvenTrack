<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transactions - InvenTrack POS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .page-container { display: flex; min-height: 100vh; }
        .sidebar { width: 250px; background: var(--dark); color: var(--white); padding: 2rem 1rem; }
        .sidebar a { color: var(--gray); text-decoration: none; display: block; padding: 0.75rem 1rem; margin-bottom: 0.5rem; border-radius: 0.5rem; transition: var(--transition); }
        .sidebar a:hover, .sidebar a.active { background: rgba(255, 255, 255, 0.1); color: var(--white); }
        .user-info { margin: 1rem; padding: 0.75rem; border: 1px solid rgba(255,255,255,0.2); border-radius: 0.5rem; }
        .logout-link { margin: 1rem; background: rgba(239, 68, 68, 0.2); color: #FCA5A5 !important; }
        .main-content { flex: 1; padding: 2rem; background: var(--light); overflow-y: auto; }
        .table-container { background: var(--white); border-radius: 0.5rem; box-shadow: var(--shadow-sm); overflow: hidden; border: 1px solid #E5E7EB; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 1rem; text-align: left; border-bottom: 1px solid #E5E7EB; }
        th { background: #F9FAFB; font-weight: 600; color: #374151; font-size: 0.875rem; text-transform: uppercase; letter-spacing: 0.05em; }
        .badge { padding: 0.25rem 0.75rem; border-radius: 9999px; font-size: 0.75rem; font-weight: 600; }
        .badge-success { background: #D1FAE5; color: #065F46; }
    </style>
</head>
<body>
    <div class="page-container">
        <!-- Sidebar -->
        <div class="sidebar">
            <h2 style="margin-bottom: 2rem; padding-left: 1rem;">InvenTrack</h2>
            <nav>
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/transactions" class="active">Transactions</a>
                <a href="${pageContext.request.contextPath}/products">Inventory</a>
                <a href="${pageContext.request.contextPath}/pos">Point of Sale</a>
            </nav>
            <div class="user-info">
                <div style="font-size: 0.75rem; color: var(--gray);">Logged in as</div>
                <div style="font-size: 0.875rem; font-weight: 600;">${sessionScope.user.fullName}</div>
                <div style="font-size: 0.75rem; color: #93C5FD;">Role: ${sessionScope.role}</div>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="logout-link">Logout</a>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 1.5rem; font-weight: 700;">Transaction History</h1>
                <p style="color: var(--gray); font-size: 0.875rem; margin-top: 0.25rem;">View all completed sales and orders.</p>
            </div>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Cashier</th>
                            <th>Date & Time</th>
                            <th>Discount</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td style="font-family: monospace;">#${order.id}</td>
                                <td>${order.cashierName}</td>
                                <td>${order.createdAt}</td>
                                <td>$${order.discount}</td>
                                <td style="font-weight: bold;">$${order.totalAmount}</td>
                                <td><span class="badge badge-success">${order.status}</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty orders}">
                            <tr>
                                <td colspan="6" style="text-align: center; color: var(--gray); padding: 3rem;">
                                    No transactions found.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  Page: Suppliers Management
  Route: GET /suppliers (SupplierServlet forwards to /suppliers.jsp)
  Submit: POST /suppliers

  Authorization:
  - Stock Manager only (enforced by SupplierServlet/AuthFilter).

  Requires:
  - requestScope.suppliers
  - requestScope.errorMessage (optional)
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Suppliers - InvenTrack POS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .page-container { display: flex; min-height: 100vh; }
        .sidebar { width: 250px; background: var(--dark); color: var(--white); padding: 2rem 1rem; }
        .sidebar a { color: var(--gray); text-decoration: none; display: block; padding: 0.75rem 1rem; margin-bottom: 0.5rem; border-radius: 0.5rem; transition: var(--transition); }
        .sidebar a:hover, .sidebar a.active { background: rgba(255, 255, 255, 0.1); color: var(--white); }
        .main-content { flex: 1; padding: 2rem; background: var(--light); overflow-y: auto; }
        .grid-layout { display: grid; grid-template-columns: 1fr 2fr; gap: 2rem; }
        .form-card { background: var(--white); padding: 1.5rem; border-radius: 0.5rem; box-shadow: var(--shadow-sm); border: 1px solid #E5E7EB; }
        .table-container { background: var(--white); border-radius: 0.5rem; box-shadow: var(--shadow-sm); overflow: hidden; border: 1px solid #E5E7EB; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 1rem; text-align: left; border-bottom: 1px solid #E5E7EB; }
        th { background: #F9FAFB; font-weight: 600; color: #374151; font-size: 0.875rem; text-transform: uppercase; letter-spacing: 0.05em; }
    </style>
</head>
<body>
    <div class="page-container">
        <!-- Sidebar -->
        <div class="sidebar">
            <h2 style="margin-bottom: 2rem; padding-left: 1rem;">InvenTrack</h2>
            <nav>
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/products" class="active">Inventory</a>
                <a href="${pageContext.request.contextPath}/categories" style="padding-left: 2rem; font-size: 0.9em;">- Categories</a>
                <a href="${pageContext.request.contextPath}/suppliers" class="active" style="padding-left: 2rem; font-size: 0.9em;">- Suppliers</a>
                <a href="${pageContext.request.contextPath}/pos">Point of Sale</a>
            </nav>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 1.5rem; font-weight: 700;">Manage Suppliers</h1>
                <p style="color: var(--gray); font-size: 0.875rem; margin-top: 0.25rem;">Add and view product suppliers.</p>
            </div>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" style="margin-bottom: 1rem;">${errorMessage}</div>
            </c:if>

            <div class="grid-layout">
                <!-- Add Supplier Form -->
                <div class="form-card">
                    <h2 style="font-size: 1.125rem; margin-bottom: 1rem;">Add Supplier</h2>
                    <form action="${pageContext.request.contextPath}/suppliers" method="POST">
                        <div class="form-group">
                            <label class="form-label">Supplier Name</label>
                            <input type="text" name="name" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Contact Info</label>
                            <input type="text" name="contactInfo" class="form-control" placeholder="Email/Phone" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Products Supplied</label>
                            <textarea name="productsSupplied" class="form-control" rows="3" placeholder="e.g. Beverages, Snacks"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Save Supplier</button>
                    </form>
                </div>

                <!-- Supplier List -->
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Contact Info</th>
                                <th>Products Supplied</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="sup" items="${suppliers}">
                                <tr>
                                    <td style="font-weight: 500;">${sup.name}</td>
                                    <td>${sup.contactInfo}</td>
                                    <td style="color: var(--gray);">${sup.productsSupplied}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty suppliers}">
                                <tr>
                                    <td colspan="3" style="text-align: center; color: var(--gray); padding: 2rem;">No suppliers found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

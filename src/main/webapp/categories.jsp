<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  Page: Categories Management
  Route: GET /categories (CategoryServlet forwards to /categories.jsp)
  Submit: POST /categories

  Authorization:
  - Stock Manager only (enforced by CategoryServlet/AuthFilter).

  Requires:
  - requestScope.categories
  - requestScope.errorMessage (optional)
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Categories - InvenTrack POS</title>
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
                <a href="${pageContext.request.contextPath}/categories" class="active" style="padding-left: 2rem; font-size: 0.9em;">- Categories</a>
                <a href="${pageContext.request.contextPath}/suppliers" style="padding-left: 2rem; font-size: 0.9em;">- Suppliers</a>
                <a href="${pageContext.request.contextPath}/pos">Point of Sale</a>
            </nav>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 1.5rem; font-weight: 700;">Manage Categories</h1>
                <p style="color: var(--gray); font-size: 0.875rem; margin-top: 0.25rem;">Add and view product categories.</p>
            </div>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" style="margin-bottom: 1rem;">${errorMessage}</div>
            </c:if>

            <div class="grid-layout">
                <!-- Add Category Form -->
                <div class="form-card">
                    <h2 style="font-size: 1.125rem; margin-bottom: 1rem;">Add Category</h2>
                    <form action="${pageContext.request.contextPath}/categories" method="POST">
                        <div class="form-group">
                            <label class="form-label">Category Name</label>
                            <input type="text" name="name" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Description</label>
                            <textarea name="description" class="form-control" rows="3"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Save Category</button>
                    </form>
                </div>

                <!-- Category List -->
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cat" items="${categories}">
                                <tr>
                                    <td>${cat.id}</td>
                                    <td style="font-weight: 500;">${cat.name}</td>
                                    <td style="color: var(--gray);">${cat.description}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty categories}">
                                <tr>
                                    <td colspan="3" style="text-align: center; color: var(--gray); padding: 2rem;">No categories found.</td>
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

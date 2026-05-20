<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  Page: Products (Inventory list)
  Route: GET /products (ProductServlet forwards to /products.jsp)

  Requires:
  - requestScope.products
  - requestScope.lowStockProducts

  Navigation:
  - "+ Add Product" links to /products?action=new (product-form.jsp)
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - InvenTrack POS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .page-container {
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            width: 250px;
            background: var(--dark);
            color: var(--white);
            padding: 2rem 1rem;
        }
        .sidebar a {
            color: var(--gray);
            text-decoration: none;
            display: block;
            padding: 0.75rem 1rem;
            margin-bottom: 0.5rem;
            border-radius: 0.5rem;
            transition: var(--transition);
        }
        .sidebar a:hover, .sidebar a.active {
            background: rgba(255, 255, 255, 0.1);
            color: var(--white);
        }
        .main-content {
            flex: 1;
            padding: 2rem;
            background: var(--light);
            overflow-y: auto;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
        }
        .table-container {
            background: var(--white);
            border-radius: 0.5rem;
            box-shadow: var(--shadow-sm);
            overflow: hidden;
            border: 1px solid #E5E7EB;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #E5E7EB;
        }
        th {
            background: #F9FAFB;
            font-weight: 600;
            color: #374151;
            font-size: 0.875rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }
        .badge {
            padding: 0.25rem 0.75rem;
            border-radius: 9999px;
            font-size: 0.75rem;
            font-weight: 600;
        }
        .badge-danger {
            background: #FEE2E2;
            color: #991B1B;
        }
        .badge-success {
            background: #D1FAE5;
            color: #065F46;
        }
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
                <a href="${pageContext.request.contextPath}/pos">Point of Sale</a>
            </nav>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <div class="header">
                <div>
                    <h1 style="font-size: 1.5rem; font-weight: 700;">Inventory Management</h1>
                    <p style="color: var(--gray); font-size: 0.875rem; margin-top: 0.25rem;">View and manage your products.</p>
                </div>
                <a href="${pageContext.request.contextPath}/products?action=new" class="btn btn-primary" style="width: auto;">+ Add Product</a>
            </div>

            <!-- Alerts for Low Stock -->
            <c:if test="${not empty lowStockProducts}">
                <div class="alert alert-danger" style="display: flex; align-items: center; justify-content: space-between;">
                    <strong>Low Stock Alert!</strong> You have ${lowStockProducts.size()} items running low.
                </div>
            </c:if>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>SKU</th>
                            <th>Product Name</th>
                            <th>Category</th>
                            <th>Supplier</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td style="font-family: monospace;">${product.sku}</td>
                                <td style="font-weight: 500;">${product.name}</td>
                                <td>${product.categoryName}</td>
                                <td>${product.supplierName}</td>
                                <td>$${product.price}</td>
                                <td>${product.stockQuantity}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.lowStock}">
                                            <span class="badge badge-danger">Low Stock</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-success">In Stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="#" style="color: var(--primary); text-decoration: none; margin-right: 1rem;">Edit</a>
                                    <a href="#" style="color: var(--danger); text-decoration: none;">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty products}">
                            <tr>
                                <td colspan="8" style="text-align: center; color: var(--gray); padding: 3rem;">
                                    No products found. Start by adding a new product!
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

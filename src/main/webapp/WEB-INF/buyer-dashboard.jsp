<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shop - InvenTrack</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .page-container { display: flex; min-height: 100vh; }
        .sidebar { width: 250px; background: var(--dark); color: var(--white); padding: 2rem 1rem; display: flex; flex-direction: column;}
        .sidebar a { color: var(--gray); text-decoration: none; display: block; padding: 0.75rem 1rem; margin-bottom: 0.5rem; border-radius: 0.5rem; transition: var(--transition); }
        .sidebar a:hover, .sidebar a.active { background: rgba(255, 255, 255, 0.1); color: var(--white); }
        .user-info { margin: 1rem; padding: 0.75rem; border: 1px solid rgba(255,255,255,0.2); border-radius: 0.5rem; }
        .logout-link { margin: 1rem; background: rgba(239, 68, 68, 0.2); color: #FCA5A5 !important; }
        .main-content { flex: 1; padding: 2rem; background: var(--light); overflow-y: auto; }

        .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 1.5rem; }
        .product-card {
            background: var(--white); border-radius: 0.5rem; padding: 1.5rem;
            text-align: center; box-shadow: var(--shadow-sm); border: 1px solid #E5E7EB;
            transition: var(--transition); display: flex; flex-direction: column; justify-content: space-between;
        }
        .product-card:hover { transform: translateY(-5px); box-shadow: var(--shadow-md); border-color: var(--primary); }
        .product-card h3 { font-size: 1.125rem; color: var(--dark); margin-bottom: 0.5rem; }
        .product-card p { font-size: 1.25rem; font-weight: 700; color: var(--primary); margin-bottom: 1rem; }
        .product-card .stock { font-size: 0.75rem; color: var(--gray); margin-bottom: 1rem; }
        .add-to-cart-btn { width: 100%; padding: 0.5rem; font-size: 0.875rem; }
    </style>
</head>
<body>
<div class="page-container">
    <!-- Sidebar -->
    <div class="sidebar">
        <h2 style="margin-bottom: 2rem; padding-left: 1rem;">InvenStore</h2>
        <nav>
            <a href="${pageContext.request.contextPath}/buyer-dashboard" class="active">Shop</a>
            <a href="${pageContext.request.contextPath}/cart">Shopping Cart</a>
            <a href="${pageContext.request.contextPath}/orders">My Orders</a>
        </nav>
        <div class="user-info" style="margin-top: auto;">
            <div style="font-size: 0.75rem; color: var(--gray);">Logged in as</div>
            <div style="font-size: 0.875rem; font-weight: 600;">${sessionScope.user.fullName}</div>
            <div style="font-size: 0.75rem; color: #93C5FD;">Role: ${sessionScope.role}</div>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="logout-link">Logout</a>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div style="margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700;">Catalog</h1>
                <p style="color: var(--gray); font-size: 0.875rem; margin-top: 0.25rem;">Browse and purchase products.</p>
            </div>
            <form action="${pageContext.request.contextPath}/buyer-dashboard" method="GET" style="display: flex; gap: 0.5rem;">
                <input type="text" name="query" class="form-control" placeholder="Search products..." value="${param.query}">
                <button type="submit" class="btn btn-primary" style="width: auto;">Search</button>
            </form>
        </div>

        <c:if test="${not empty sessionScope.checkoutSuccess}">
            <div class="alert alert-success" style="margin-bottom: 1rem;">${sessionScope.checkoutSuccess}</div>
            <c:remove var="checkoutSuccess" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.checkoutError}">
            <div class="alert alert-danger" style="margin-bottom: 1rem;">${sessionScope.checkoutError}</div>
            <c:remove var="checkoutError" scope="session"/>
        </c:if>

        <div class="product-grid">
            <c:forEach var="product" items="${products}">
                <div class="product-card">
                    <div>
                        <h3>${product.name}</h3>
                        <p>$${product.price}</p>
                        <div class="stock">Stock: ${product.stockQuantity} ${product.unitOfMeasure}</div>
                    </div>
                    <form action="${pageContext.request.contextPath}/cart" method="POST">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.id}">
                        <input type="hidden" name="quantity" value="1">
                        <input type="hidden" name="returnTo" value="buyer-dashboard">
                        <button type="submit" class="btn btn-primary add-to-cart-btn" ${product.stockQuantity <= 0 ? 'disabled' : ''}>
                                ${product.stockQuantity <= 0 ? 'Out of Stock' : 'Add to Cart'}
                        </button>
                    </form>
                </div>
            </c:forEach>
            <c:if test="${empty products}">
                <p style="color: var(--gray);">No products found.</p>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>
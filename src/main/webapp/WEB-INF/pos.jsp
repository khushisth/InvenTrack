<%--
  Created by IntelliJ IDEA.
  User: amick Khada
  Date: 5/3/2026
  Time: 8:08 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>POS Terminal - InvenTrack</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .pos-container {
            display: flex;
            height: 100vh;
            overflow: hidden;
            background: #F3F4F6;
        }
        /* Left Side: Products */
        .pos-products {
            flex: 1;
            display: flex;
            flex-direction: column;
            padding: 1.5rem;
            overflow-y: auto;
        }
        .search-bar {
            margin-bottom: 1.5rem;
            display: flex;
            gap: 1rem;
        }
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 1.5rem;
        }
        .product-card {
            background: var(--white);
            border-radius: 0.5rem;
            padding: 1.5rem;
            text-align: center;
            box-shadow: var(--shadow-sm);
            border: 1px solid #E5E7EB;
            cursor: pointer;
            transition: var(--transition);
        }
        .product-card:hover {
            transform: translateY(-3px);
            box-shadow: var(--shadow-md);
            border-color: var(--primary);
        }
        .product-card h3 { font-size: 1rem; color: var(--dark); margin-bottom: 0.5rem; }
        .product-card p { font-size: 1.125rem; font-weight: 700; color: var(--primary); margin-bottom: 1rem; }
        .product-card .stock { font-size: 0.75rem; color: var(--gray); }

        /* Right Side: Cart */
        .pos-cart {
            width: 380px;
            background: var(--white);
            box-shadow: -4px 0 15px rgba(0,0,0,0.05);
            display: flex;
            flex-direction: column;
            z-index: 10;
        }
        .cart-header {
            padding: 1.5rem;
            border-bottom: 1px solid #E5E7EB;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .cart-items {
            flex: 1;
            overflow-y: auto;
            padding: 1.5rem;
        }
        .cart-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-bottom: 1rem;
            margin-bottom: 1rem;
            border-bottom: 1px dashed #E5E7EB;
        }
        .cart-item-info h4 { font-size: 0.875rem; margin-bottom: 0.25rem; }
        .cart-item-info p { font-size: 0.75rem; color: var(--gray); }
        .cart-item-actions { display: flex; align-items: center; gap: 0.5rem; }
        .qty-input { width: 40px; text-align: center; border: 1px solid #D1D5DB; border-radius: 0.25rem; padding: 0.25rem; }
        .cart-totals {
            padding: 1.5rem;
            background: #F9FAFB;
            border-top: 1px solid #E5E7EB;
        }
        .totals-row { display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.875rem; }
        .totals-row.grand-total { font-size: 1.25rem; font-weight: 700; color: var(--dark); margin-top: 1rem; padding-top: 1rem; border-top: 1px solid #E5E7EB; }
        .checkout-btn { width: 100%; padding: 1rem; font-size: 1.125rem; margin-top: 1rem; }
    </style>
</head>
<body>
<div class="pos-container">

    <!-- Products Area -->
    <div class="pos-products">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
            <h1 style="font-size: 1.5rem; font-weight: 700;">Terminal</h1>
            <a href="${pageContext.request.contextPath}/dashboard" style="color: var(--gray); text-decoration: none; font-size: 0.875rem;">← Back to Dashboard</a>
        </div>

        <c:if test="${not empty sessionScope.checkoutSuccess}">
            <div class="alert alert-success">${sessionScope.checkoutSuccess}</div>
            <c:remove var="checkoutSuccess" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.checkoutError}">
            <div class="alert alert-danger">${sessionScope.checkoutError}</div>
            <c:remove var="checkoutError" scope="session"/>
        </c:if>

        <form action="${pageContext.request.contextPath}/pos" method="GET" class="search-bar">
            <input type="text" name="query" class="form-control" placeholder="Search by name, SKU, or barcode..." value="${param.query}" autofocus>
            <button type="submit" class="btn btn-primary" style="width: auto;">Search</button>
        </form>

        <div class="product-grid">
            <c:forEach var="product" items="${products}">
                <form action="${pageContext.request.contextPath}/cart" method="POST" style="display: inline;">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="${product.id}">
                    <input type="hidden" name="quantity" value="1">
                    <div class="product-card" onclick="this.parentNode.submit();">
                        <h3>${product.name}</h3>
                        <p>$${product.price}</p>
                        <span class="stock">Stock: ${product.stockQuantity} ${product.unitOfMeasure}</span>
                    </div>
                </form>
            </c:forEach>
            <c:if test="${empty products}">
                <p style="color: var(--gray);">No products found.</p>
            </c:if>
        </div>
    </div>

    <!-- Cart Area -->
    <div class="pos-cart">
        <div class="cart-header">
            <h2 style="font-size: 1.125rem; font-weight: 600;">Current Order</h2>
            <form action="${pageContext.request.contextPath}/cart" method="POST" style="margin: 0;">
                <input type="hidden" name="action" value="clear">
                <button type="submit" style="background: none; border: none; color: var(--danger); cursor: pointer; font-size: 0.875rem;">Clear All</button>
            </form>
        </div>

        <div class="cart-items">
            <c:set var="cart" value="${sessionScope.cart}" />
            <c:if test="${empty cart or empty cart.items}">
                <div style="text-align: center; color: var(--gray); margin-top: 2rem;">
                    <p>Cart is empty</p>
                </div>
            </c:if>

            <c:if test="${not empty cart and not empty cart.items}">
                <c:forEach var="item" items="${cart.items}">
                    <div class="cart-item">
                        <div class="cart-item-info">
                            <h4>${item.product.name}</h4>
                            <p>$${item.product.price} x ${item.quantity}</p>
                        </div>
                        <div class="cart-item-actions">
                            <span style="font-weight: 600; font-size: 0.875rem; margin-right: 0.5rem;">$${item.subtotal}</span>
                            <form action="${pageContext.request.contextPath}/cart" method="POST" style="margin: 0;">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="productId" value="${item.product.id}">
                                <button type="submit" style="background: none; border: none; color: var(--danger); cursor: pointer;">✕</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </div>

        <div class="cart-totals">
            <div class="totals-row">
                <span>Subtotal</span>
                <span>$${not empty cart ? cart.subtotal : '0.00'}</span>
            </div>
            <div class="totals-row">
                <span>Discount</span>
                <span>$${not empty cart ? cart.discount : '0.00'}</span>
            </div>
            <div class="totals-row grand-total">
                <span>Total</span>
                <span>$${not empty cart ? cart.total : '0.00'}</span>
            </div>

            <form action="${pageContext.request.contextPath}/cart" method="POST">
                <input type="hidden" name="action" value="checkout">
                <button type="submit" class="btn btn-primary checkout-btn" ${empty cart or empty cart.items ? 'disabled style="opacity:0.5;cursor:not-allowed;"' : ''}>
                    Checkout ->
                </button>
            </form>
        </div>
    </div>

</div>
</body>
</html>


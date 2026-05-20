<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - InvenTrack POS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .page-container { display: flex; min-height: 100vh; }
        .sidebar { width: 250px; background: var(--dark); color: var(--white); padding: 2rem 1rem; display: flex; flex-direction: column; }
        .sidebar a { color: var(--gray); text-decoration: none; display: block; padding: 0.75rem 1rem; margin-bottom: 0.5rem; border-radius: 0.5rem; transition: var(--transition); }
        .sidebar a:hover, .sidebar a.active { background: rgba(255, 255, 255, 0.1); color: var(--white); }
        .user-info { margin: 1rem; padding: 0.75rem; border: 1px solid rgba(255,255,255,0.2); border-radius: 0.5rem; }
        .logout-link { margin: 1rem; background: rgba(239, 68, 68, 0.2); color: #FCA5A5 !important; }
        .main-content { flex: 1; padding: 2rem; background: var(--light); overflow-y: auto; }
        .cart-layout { display: grid; grid-template-columns: 1fr 340px; gap: 2rem; align-items: start; }
        .cart-card { background: var(--white); border-radius: 0.5rem; box-shadow: var(--shadow-sm); border: 1px solid #E5E7EB; overflow: hidden; }
        .cart-item-row {
            display: grid;
            grid-template-columns: 1fr auto auto auto;
            gap: 1rem;
            align-items: center;
            padding: 1rem 1.5rem;
            border-bottom: 1px solid #E5E7EB;
        }
        .cart-item-name { font-weight: 600; color: var(--dark); }
        .cart-item-meta { font-size: 0.875rem; color: var(--gray); margin-top: 0.25rem; }
        .cart-summary { padding: 1.5rem; }
        .totals-row { display: flex; justify-content: space-between; margin-bottom: 0.75rem; font-size: 0.875rem; color: #374151; }
        .totals-row.grand-total {
            font-size: 1.25rem;
            font-weight: 700;
            color: var(--dark);
            margin-top: 1rem;
            padding-top: 1rem;
            border-top: 1px solid #E5E7EB;
        }
        .empty-cart { text-align: center; padding: 4rem 2rem; color: var(--gray); }
        .btn-remove {
            background: none;
            border: none;
            color: var(--danger);
            cursor: pointer;
            font-size: 0.875rem;
            padding: 0.25rem 0.5rem;
        }
        .btn-remove:hover { text-decoration: underline; }
        .cart-header-bar {
            display: grid;
            grid-template-columns: 1fr auto auto auto;
            gap: 1rem;
            padding: 1rem 1.5rem;
            background: #F9FAFB;
            border-bottom: 1px solid #E5E7EB;
            font-size: 0.875rem;
            font-weight: 600;
            color: #374151;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }
        @media (max-width: 900px) {
            .cart-layout { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <div class="page-container">
        <div class="sidebar">
            <h2 style="margin-bottom: 2rem; padding-left: 1rem;">InvenTrack</h2>
            <nav>
                <c:choose>
                    <c:when test="${sessionScope.role eq 'Buyer'}">
                        <a href="${pageContext.request.contextPath}/buyer-dashboard">Shop</a>
                        <a href="${pageContext.request.contextPath}/cart" class="active">Shopping Cart</a>
                        <a href="${pageContext.request.contextPath}/orders">My Orders</a>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${sessionScope.role eq 'Admin'}">
                            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/pos">Point of Sale</a>
                        <a href="${pageContext.request.contextPath}/cart" class="active">Shopping Cart</a>
                        <c:if test="${sessionScope.role eq 'Stock Manager'}">
                            <a href="${pageContext.request.contextPath}/products">Inventory</a>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </nav>
            <div class="user-info" style="margin-top: auto;">
                <div style="font-size: 0.75rem; color: var(--gray);">Logged in as</div>
                <div style="font-size: 0.875rem; font-weight: 600;">${sessionScope.user.fullName}</div>
                <div style="font-size: 0.75rem; color: #93C5FD;">Role: ${sessionScope.role}</div>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="logout-link">Logout</a>
        </div>

        <div class="main-content">
            <div style="margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h1 style="font-size: 1.5rem; font-weight: 700;">Shopping Cart</h1>
                    <p style="color: var(--gray); font-size: 0.875rem; margin-top: 0.25rem;">Review items before checkout.</p>
                </div>
                <c:choose>
                    <c:when test="${sessionScope.role eq 'Buyer'}">
                        <a href="${pageContext.request.contextPath}/buyer-dashboard" class="btn btn-primary" style="width: auto;">+ Add More Items</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/pos" class="btn btn-primary" style="width: auto;">+ Add More Items</a>
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="${not empty sessionScope.checkoutSuccess}">
                <div class="alert alert-success" style="margin-bottom: 1rem;">${sessionScope.checkoutSuccess}</div>
                <c:remove var="checkoutSuccess" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.checkoutError}">
                <div class="alert alert-danger" style="margin-bottom: 1rem;">${sessionScope.checkoutError}</div>
                <c:remove var="checkoutError" scope="session"/>
            </c:if>

            <c:set var="cart" value="${sessionScope.cart}" />

            <div class="cart-layout">
                <div class="cart-card">
                    <c:choose>
                        <c:when test="${empty cart or empty cart.items}">
                            <div class="empty-cart">
                                <p style="font-size: 1.125rem; margin-bottom: 0.5rem;">Your cart is empty</p>
                                <p style="font-size: 0.875rem; margin-bottom: 1.5rem;">Browse products at the POS terminal to add items.</p>
                                <c:choose>
                                    <c:when test="${sessionScope.role eq 'Buyer'}">
                                        <a href="${pageContext.request.contextPath}/buyer-dashboard" class="btn btn-primary" style="width: auto; display: inline-block;">Go to Shop</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/pos" class="btn btn-primary" style="width: auto; display: inline-block;">Go to POS</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="cart-header-bar">
                                <span>Product</span>
                                <span>Unit Price</span>
                                <span>Qty</span>
                                <span>Subtotal</span>
                            </div>
                            <c:forEach var="item" items="${cart.items}">
                                <div class="cart-item-row">
                                    <div>
                                        <div class="cart-item-name">${item.product.name}</div>
                                        <div class="cart-item-meta">SKU: ${item.product.sku}</div>
                                    </div>
                                    <span>$${item.product.price}</span>
                                    <span>${item.quantity}</span>
                                    <div style="display: flex; align-items: center; gap: 0.75rem;">
                                        <span style="font-weight: 600;">$${item.subtotal}</span>
                                        <form action="${pageContext.request.contextPath}/cart" method="POST" style="margin: 0;">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="productId" value="${item.product.id}">
                                            <input type="hidden" name="returnTo" value="cart">
                                            <button type="submit" class="btn-remove" title="Remove item">Remove</button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                            <div style="padding: 1rem 1.5rem; border-top: 1px solid #E5E7EB; text-align: right;">
                                <form action="${pageContext.request.contextPath}/cart" method="POST" style="display: inline;">
                                    <input type="hidden" name="action" value="clear">
                                    <input type="hidden" name="returnTo" value="cart">
                                    <button type="submit" class="btn-remove">Clear entire cart</button>
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="cart-card cart-summary">
                    <h2 style="font-size: 1.125rem; font-weight: 600; margin-bottom: 1.25rem;">Order Summary</h2>
                    <div class="totals-row">
                        <span>Items</span>
                        <span>${not empty cart ? cart.items.size() : 0}</span>
                    </div>
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

                    <form action="${pageContext.request.contextPath}/cart" method="POST" style="margin-top: 1.5rem;">
                        <input type="hidden" name="action" value="checkout">
                        <input type="hidden" name="returnTo" value="cart">
                        <c:choose>
                            <c:when test="${empty cart or empty cart.items}">
                                <button type="submit" class="btn btn-primary" style="width: 100%; opacity: 0.5; cursor: not-allowed;" disabled>Complete Checkout</button>
                            </c:when>
                            <c:otherwise>
                                <button type="submit" class="btn btn-primary" style="width: 100%;">Complete Checkout</button>
                            </c:otherwise>
                        </c:choose>
                    </form>
                    <c:choose>
                        <c:when test="${sessionScope.role eq 'Buyer'}">
                            <a href="${pageContext.request.contextPath}/buyer-dashboard" style="display: block; text-align: center; margin-top: 1rem; color: var(--gray); font-size: 0.875rem; text-decoration: none;">Continue shopping</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/pos" style="display: block; text-align: center; margin-top: 1rem; color: var(--gray); font-size: 0.875rem; text-decoration: none;">Continue shopping</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

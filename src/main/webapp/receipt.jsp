<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=80mm, initial-scale=1.0">
    <title>Receipt</title>
    <style>
        body {
            font-family: 'Courier New', Courier, monospace;
            width: 80mm; /* Standard receipt printer width */
            margin: 0 auto;
            padding: 10px;
            color: #000;
            background: #fff;
        }
        .header { text-align: center; margin-bottom: 20px; }
        .header h1 { font-size: 1.2rem; margin: 0; }
        .header p { font-size: 0.8rem; margin: 2px 0; }
        .divider { border-bottom: 1px dashed #000; margin: 10px 0; }
        .item-row { display: flex; justify-content: space-between; font-size: 0.9rem; margin-bottom: 5px; }
        .item-name { flex: 1; }
        .totals { margin-top: 15px; }
        .totals-row { display: flex; justify-content: space-between; font-size: 0.9rem; font-weight: bold; }
        .footer { text-align: center; margin-top: 30px; font-size: 0.8rem; }
        @media print {
            .no-print { display: none; }
            body { width: 100%; margin: 0; padding: 0; }
        }
        .btn {
            display: block; width: 100%; padding: 10px; text-align: center;
            background: #4F46E5; color: white; text-decoration: none;
            border-radius: 5px; font-family: sans-serif; margin-top: 20px;
            box-sizing: border-box;
        }
        .btn-outline {
            background: white; color: #4F46E5; border: 1px solid #4F46E5;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>INVENTRACK POS</h1>
        <p>123 Store Address, City</p>
        <p>Tel: +1 234 567 890</p>
        <div class="divider"></div>
        <p>Date: <%= new java.util.Date() %></p>
        <p>Cashier: ${sessionScope.user.fullName}</p>
    </div>

    <div class="divider"></div>

    <c:set var="cart" value="${sessionScope.cart}" />
    <c:forEach var="item" items="${cart.items}">
        <div class="item-row">
            <span class="item-name">${item.quantity}x ${item.product.name}</span>
            <span>$${item.subtotal}</span>
        </div>
    </c:forEach>

    <div class="divider"></div>

    <div class="totals">
        <div class="totals-row">
            <span>Subtotal:</span>
            <span>$${cart.subtotal}</span>
        </div>
        <div class="totals-row">
            <span>Discount:</span>
            <span>$${cart.discount}</span>
        </div>
        <div class="divider"></div>
        <div class="totals-row" style="font-size: 1.1rem;">
            <span>TOTAL:</span>
            <span>$${cart.total}</span>
        </div>
    </div>

    <div class="divider"></div>
    <div class="footer">
        <p>Thank you for your purchase!</p>
        <p>Please come again.</p>
    </div>

    <div class="no-print">
        <button class="btn" onclick="window.print()">Print Receipt</button>
        <a href="${pageContext.request.contextPath}/pos?action=finish" class="btn btn-outline">New Sale</a>
    </div>
</body>
</html>

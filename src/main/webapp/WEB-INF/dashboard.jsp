<%--
  Created by IntelliJ IDEA.
  User: amick Khada
  Date: 5/14/2026
  Time: 12:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard - InvenTrack POS</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <style>
    .page-container { display: flex; min-height: 100vh; }
    .sidebar { width: 250px; background: var(--dark); color: var(--white); padding: 2rem 1rem; }
    .sidebar a { color: var(--gray); text-decoration: none; display: block; padding: 0.75rem 1rem; margin-bottom: 0.5rem; border-radius: 0.5rem; transition: var(--transition); }
    .sidebar a:hover, .sidebar a.active { background: rgba(255, 255, 255, 0.1); color: var(--white); }
    .user-info { margin: 1rem; padding: 0.75rem; border: 1px solid rgba(255,255,255,0.2); border-radius: 0.5rem; }
    .logout-link { margin: 1rem; background: rgba(239, 68, 68, 0.2); color: #FCA5A5 !important; }
    .main-content { flex: 1; padding: 2rem; background: var(--light); overflow-y: auto; }

    .metric-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
    }
    .metric-card {
      background: var(--white);
      padding: 1.5rem;
      border-radius: 0.5rem;
      box-shadow: var(--shadow-sm);
      border: 1px solid #E5E7EB;
      display: flex;
      align-items: center;
      transition: var(--transition);
    }
    .metric-card:hover { transform: translateY(-5px); box-shadow: var(--shadow-md); }
    .metric-icon {
      width: 3rem; height: 3rem;
      border-radius: 50%;
      display: flex; align-items: center; justify-content: center;
      margin-right: 1rem;
      font-size: 1.5rem; color: var(--white);
    }
    .icon-revenue { background: var(--primary); }
    .icon-orders { background: var(--secondary); }
    .icon-products { background: var(--warning); }
    .icon-alerts { background: var(--danger); }
    .metric-info h3 { font-size: 0.875rem; color: var(--gray); font-weight: 500; }
    .metric-info p { font-size: 1.5rem; font-weight: 700; color: var(--dark); }
  </style>
</head>
<body>
<div class="page-container">
  <!-- Sidebar -->
  <div class="sidebar">
    <h2 style="margin-bottom: 2rem; padding-left: 1rem;">InvenTrack</h2>
    <nav>
      <a href="${pageContext.request.contextPath}/dashboard" class="active">Dashboard</a>
      <a href="${pageContext.request.contextPath}/products">Inventory</a>
      <a href="${pageContext.request.contextPath}/pos">Point of Sale</a>
      <c:if test="${sessionScope.role eq 'Admin'}">
        <a href="${pageContext.request.contextPath}/users">User Management</a>
      </c:if>
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
    <div style="margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: center;">
      <div>
        <h1 style="font-size: 1.5rem; font-weight: 700;">Dashboard Overview</h1>
        <p style="color: var(--gray); font-size: 0.875rem; margin-top: 0.25rem;">Welcome back, ${sessionScope.user.fullName}!</p>
      </div>
    </div>

    <div class="metric-grid">
      <div class="metric-card">
        <div class="metric-icon icon-revenue">$</div>
        <div class="metric-info">
          <h3>Total Revenue</h3>
          <p>$${totalRevenue}</p>
        </div>
      </div>
      <div class="metric-card">
        <div class="metric-icon icon-orders">🛒</div>
        <div class="metric-info">
          <h3>Total Orders</h3>
          <p>${totalOrders}</p>
        </div>
      </div>
      <div class="metric-card">
        <div class="metric-icon icon-products">📦</div>
        <div class="metric-info">
          <h3>Total Products</h3>
          <p>${totalProducts}</p>
        </div>
      </div>
      <div class="metric-card">
        <div class="metric-icon icon-alerts">⚠️</div>
        <div class="metric-info">
          <h3>Low Stock Alerts</h3>
          <p>${lowStockCount}</p>
        </div>
      </div>
    </div>
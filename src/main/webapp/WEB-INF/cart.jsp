<%--
  Created by IntelliJ IDEA.
  User: amick Khada
  Date: 5/15/2026
  Time: 9:10 PM
  To change this template use File | Settings | File Templates.
--%>
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

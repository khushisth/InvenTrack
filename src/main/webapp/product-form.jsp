<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  Page: Add Product Form
  Route: GET /products?action=new (ProductServlet forwards to /product-form.jsp)
  Submit: POST /products

  Requires:
  - requestScope.categories (for category dropdown)
  - requestScope.suppliers (for supplier dropdown)
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Product - InvenTrack POS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .page-container { display: flex; min-height: 100vh; }
        .sidebar { width: 250px; background: var(--dark); color: var(--white); padding: 2rem 1rem; }
        .sidebar a { color: var(--gray); text-decoration: none; display: block; padding: 0.75rem 1rem; margin-bottom: 0.5rem; border-radius: 0.5rem; transition: var(--transition); }
        .sidebar a:hover, .sidebar a.active { background: rgba(255, 255, 255, 0.1); color: var(--white); }
        .main-content { flex: 1; padding: 2rem; background: var(--light); overflow-y: auto; }
        
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1.5rem;
        }
        .form-card {
            background: var(--white);
            padding: 2rem;
            border-radius: 0.5rem;
            box-shadow: var(--shadow-sm);
            border: 1px solid #E5E7EB;
            max-width: 800px;
        }
        .form-select {
            width: 100%;
            padding: 0.75rem 1rem;
            border: 1px solid #D1D5DB;
            border-radius: 0.5rem;
            font-size: 1rem;
            font-family: inherit;
            background: rgba(255, 255, 255, 0.9);
        }
        .full-width { grid-column: span 2; }
        .form-actions {
            margin-top: 2rem;
            display: flex;
            justify-content: flex-end;
            gap: 1rem;
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
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 1.5rem; font-weight: 700;">Add New Product</h1>
                <p style="color: var(--gray); font-size: 0.875rem; margin-top: 0.25rem;">Fill out the details to add a new item to inventory.</p>
            </div>

            <div class="form-card">
                <form action="${pageContext.request.contextPath}/products" method="POST">
                    <div class="form-grid">
                        <div class="form-group full-width">
                            <label class="form-label">Product Name</label>
                            <input type="text" name="name" class="form-control" required>
                        </div>
                        
                        <div class="form-group">
                            <label class="form-label">SKU</label>
                            <input type="text" name="sku" class="form-control" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Barcode</label>
                            <input type="text" name="barcode" class="form-control">
                        </div>

                        <div class="form-group">
                            <label class="form-label">Category</label>
                            <select name="categoryId" class="form-select" required>
                                <option value="">Select Category</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Supplier</label>
                            <select name="supplierId" class="form-select" required>
                                <option value="">Select Supplier</option>
                                <c:forEach var="supplier" items="${suppliers}">
                                    <option value="${supplier.id}">${supplier.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Selling Price</label>
                            <input type="number" step="0.01" name="price" class="form-control" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Cost Price</label>
                            <input type="number" step="0.01" name="costPrice" class="form-control" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Initial Stock Quantity</label>
                            <input type="number" name="stockQuantity" class="form-control" value="0" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Reorder Level (Alert)</label>
                            <input type="number" name="reorderLevel" class="form-control" value="10" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Shelf Location</label>
                            <input type="text" name="shelfLocation" class="form-control">
                        </div>

                        <div class="form-group">
                            <label class="form-label">Unit of Measure (e.g., pcs, kg, box)</label>
                            <input type="text" name="unitOfMeasure" class="form-control" value="pcs">
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary" style="width: auto; background: var(--gray);">Cancel</a>
                        <button type="submit" class="btn btn-primary" style="width: auto;">Save Product</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>

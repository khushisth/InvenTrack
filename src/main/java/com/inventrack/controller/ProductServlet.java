package com.inventrack.controller;

import com.inventrack.dao.CategoryDAO;
import com.inventrack.dao.SupplierDAO;
import com.inventrack.model.Product;
import com.inventrack.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Inventory (products) controller.
 *
 * <p>URL mapping: {@code /products}</p>
 *
 * <p>GET supports:</p>
 * <ul>
 *   <li>{@code action=list} (default): list products</li>
 *   <li>{@code action=new}: show the "add product" form</li>
 * </ul>
 *
 * <p>POST: create a new product from submitted form fields.</p>
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductService productService;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        categoryDAO = new CategoryDAO();
        supplierDAO = new SupplierDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Determine requested action.
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        // Step 2: Dispatch based on action.
        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Load product list + low stock subset.
        request.setAttribute("products", productService.getAllProducts());
        request.setAttribute("lowStockProducts", productService.getLowStockProducts());
        // Step 2: Render JSP.
        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Load lookup lists for select dropdowns.
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.setAttribute("suppliers", supplierDAO.getAllSuppliers());
        // Step 2: Render JSP form.
        request.getRequestDispatcher("/product-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Read and parse product fields from the form.
        String name = request.getParameter("name");
        String sku = request.getParameter("sku");
        String barcode = request.getParameter("barcode");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int supplierId = Integer.parseInt(request.getParameter("supplierId"));
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        BigDecimal costPrice = new BigDecimal(request.getParameter("costPrice"));
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
        int reorderLevel = Integer.parseInt(request.getParameter("reorderLevel"));
        String shelfLocation = request.getParameter("shelfLocation");
        String unitOfMeasure = request.getParameter("unitOfMeasure");

        // Step 2: Build Product model object.
        Product p = new Product();
        p.setName(name);
        p.setSku(sku);
        p.setBarcode(barcode);
        p.setCategoryId(categoryId);
        p.setSupplierId(supplierId);
        p.setPrice(price);
        p.setCostPrice(costPrice);
        p.setStockQuantity(stockQuantity);
        p.setReorderLevel(reorderLevel);
        p.setShelfLocation(shelfLocation);
        p.setUnitOfMeasure(unitOfMeasure);

        // Step 3: Persist and redirect back to inventory list.
        productService.addProduct(p);
        response.sendRedirect(request.getContextPath() + "/products");
    }
}

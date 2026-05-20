package com.inventrack.service;

import com.inventrack.dao.ProductDAO;
import com.inventrack.model.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Product/inventory business logic.
 *
 * <p>This service is a thin layer on top of {@link ProductDAO} and provides a couple of convenience
 * operations (search and low-stock filtering) used by the inventory and POS screens.</p>
 */
public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    /**
     * @return all products with category/supplier names populated (via joins in the DAO)
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Creates a new product record.
     *
     * @param product product details from the product form
     * @return true if inserted successfully
     */
    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }
    
    /**
     * Searches by product name, SKU, or barcode.
     *
     * @param query raw search query
     * @return matching products (empty list if no matches)
     */
    public List<Product> searchProducts(String query) {
        return productDAO.searchProducts(query);
    }
    
    /**
     * @return products whose {@code stockQuantity <= reorderLevel}
     */
    public List<Product> getLowStockProducts() {
        return productDAO.getAllProducts()
                .stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
    }
}

package com.inventrack.service;

import com.inventrack.dao.ProductDAO;
import com.inventrack.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }
    
    public List<Product> searchProducts(String query) {
        return productDAO.searchProducts(query);
    }
    
    public List<Product> getLowStockProducts() {
        return productDAO.getAllProducts()
                .stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
    }
}

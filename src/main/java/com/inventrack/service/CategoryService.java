package com.inventrack.service;

import com.inventrack.dao.CategoryDAO;
import com.inventrack.model.Category;

import java.util.List;

/**
 * Category management business logic.
 *
 * <p>Categories are used to group products for inventory organization.</p>
 */
public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    /**
     * @return all product categories
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    /**
     * Adds a new category after minimal validation.
     *
     * @param category category data from the categories screen
     * @return true if the record was inserted; false otherwise
     */
    public boolean addCategory(Category category) {
        // Step 1: Basic input validation (DB also enforces constraints).
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }
        // Step 2: Persist via DAO.
        return categoryDAO.addCategory(category);
    }
}

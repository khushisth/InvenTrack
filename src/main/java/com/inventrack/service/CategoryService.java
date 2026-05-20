package com.inventrack.service;

import com.inventrack.dao.CategoryDAO;
import com.inventrack.model.Category;

import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public boolean addCategory(Category category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }
        return categoryDAO.addCategory(category);
    }
}

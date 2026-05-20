package com.inventrack.service;

import com.inventrack.dao.SupplierDAO;
import com.inventrack.model.Supplier;

import java.util.List;

/**
 * Supplier management business logic.
 *
 * <p>Suppliers are used to track where products are sourced from.</p>
 */
public class SupplierService {
    private final SupplierDAO supplierDAO;

    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
    }

    /**
     * @return all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    /**
     * Adds a supplier after minimal validation.
     *
     * @param supplier supplier data from the suppliers screen
     * @return true if the record was inserted; false otherwise
     */
    public boolean addSupplier(Supplier supplier) {
        // Step 1: Basic input validation.
        if (supplier == null || supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            return false;
        }
        // Step 2: Persist via DAO.
        return supplierDAO.addSupplier(supplier);
    }
}

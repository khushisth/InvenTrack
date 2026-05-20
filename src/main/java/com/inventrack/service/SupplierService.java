package com.inventrack.service;

import com.inventrack.dao.SupplierDAO;
import com.inventrack.model.Supplier;

import java.util.List;

public class SupplierService {
    private final SupplierDAO supplierDAO;

    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
    }

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public boolean addSupplier(Supplier supplier) {
        if (supplier == null || supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            return false;
        }
        return supplierDAO.addSupplier(supplier);
    }
}

package service;

import model.Supplier;
import model.dao.SupplierDAO;

import java.util.List;

public class SupplierService {
    private final model.dao.SupplierDAO supplierDAO;

    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
    }

    public List<model.Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public boolean addSupplier(Supplier supplier) {
        if (supplier == null || supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            return false;
        }
        return supplierDAO.addSupplier(supplier);
    }
}

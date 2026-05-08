package controller;
import model.Supplier;

import java.io.IOException;

@WebServlet("/suppliers")
public class SupplierServlet extends HttpServlet {
    private SupplierService supplierService;

    @Override
    public void init() throws ServletException {
        supplierService = new SupplierService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("suppliers", supplierService.getAllSuppliers());
        request.getRequestDispatcher("/suppliers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String contactInfo = request.getParameter("contactInfo");
        String productsSupplied = request.getParameter("productsSupplied");

        Supplier supplier = new Supplier(0, name, contactInfo, productsSupplied);
        supplierService.addSupplier(supplier);

        response.sendRedirect(request.getContextPath() + "/suppliers");
    }
}
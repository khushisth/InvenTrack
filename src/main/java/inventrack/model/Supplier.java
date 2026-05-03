package model;

public class Supplier {
    private int id;
    private String name;
    private String contactInfo;
    private String productsSupplied;

    public Supplier() {}

    public Supplier(int id, String name, String contactInfo, String productsSupplied) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.productsSupplied = productsSupplied;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getProductsSupplied() { return productsSupplied; }
    public void setProductsSupplied(String productsSupplied) { this.productsSupplied = productsSupplied; }
}

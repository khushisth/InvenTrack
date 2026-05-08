package model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Transaction {
    private int id;
    private String transactionNumber;
    private int userId;
    private String userName;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal finalAmount;
    private String paymentMethod;
    private String status;
    private Date transactionDate;
    private List<TransactionItem> items;

    public Transaction() {
        this.discountAmount = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.status = "completed";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTransactionNumber() { return transactionNumber; }
    public void setTransactionNumber(String transactionNumber) { this.transactionNumber = transactionNumber; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public List<TransactionItem> getItems() { return items; }
    public void setItems(List<TransactionItem> items) { this.items = items; }
}
transcation
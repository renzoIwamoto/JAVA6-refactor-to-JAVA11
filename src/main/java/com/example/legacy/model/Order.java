package com.example.legacy.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@XmlRootElement
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Customer customer;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Date orderDate;
    private String status;
    
    public Order() {
    }
    
    public Order(Long id, Customer customer, String productName, Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.customer = customer;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderDate = new Date();
        this.status = "PENDING";
    }
    
    @XmlElement
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @XmlElement
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @XmlElement
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    @XmlElement
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    @XmlElement
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    @XmlElement
    public Date getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    
    @XmlElement
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getTotalAmount() {
        if (quantity != null && unitPrice != null) {
            return unitPrice.multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
    
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", total=" + getTotalAmount() +
                '}';
    }
}
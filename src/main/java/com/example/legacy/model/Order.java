package com.example.legacy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    private String id;
    private String customerId;
    private double amount;
    private Date createdAt;

    public Order() {}
    public Order(String id, String customerId, double amount, Date createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public double getAmount() { return amount; }
    public Date getCreatedAt() { return createdAt; }

    public void setId(String id) { this.id = id; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

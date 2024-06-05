package org.example.bs;

import java.util.Date;

public class SalesData {
    private int id;
    private String ID;
    private String Name;
    private String Type;
    private Double price;
    private Integer Quantity;
    private Date date;

    public void setId(int id) {
        this.id = id;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getid() {
        return id;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getType() {
        return Type;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public Date getDate() {
        return date;
    }
}

package org.example.bs;

import java.util.Date;

public class ProductDeta {
    private int num;
    private String namee;
    private Double pricee;

    public int getId() {
        return num;
    }

    public String getName() {
        return namee;
    }

    public Double getPrice() {
        return pricee;
    }

    public ProductDeta( String Name, Double price)
    {
        this.namee = Name;
        this.pricee = price;
    }
}

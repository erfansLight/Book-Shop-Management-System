package org.example.bs;

import java.util.Date;

public class CustomerDeta  {
    private int id;
    private String ID;
    private String Name;
    private String Type;
    private Double price;
    private Integer Quantity;
    private Date date;

    public CustomerDeta(int id, String ID,
                        String Name,String Type,
                        Double price, Integer Quantity,Date date)
    {
        this.id = id;
        this.ID = ID;
        this.Name = Name;
        this.Type = Type;
        this.price = price;
        this.Quantity = Quantity;
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

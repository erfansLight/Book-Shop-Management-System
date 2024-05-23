package org.example.bs;

import java.util.Date;

public class BookDeta {
    private int id;
    private String ID;
    private String Name;
    private String Description;
    private Double price;
    private String type;
    private String Author;
    private Date date;

    public BookDeta(int id, String ID,
                    String Name,String type,
                    String Description, Double price,
                    String Author, Date date)
    {
        this.id = id;
        this.ID = ID;
        this.Name = Name;
        this.type = type;
        this.Description = Description;
        this.price = price;
        this.Author = Author;
        this.date = date;

    }

    public int getId() {
        return id;
    }

    public String getID() {
        return ID;
    }
    public String getType() {
        return type;
    }

    public String getName() {
        return Name;
    }

    public String  getAuthor() {
        return Author;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return Description;
    }

    public Date getDate() {
        return date;
    }
}

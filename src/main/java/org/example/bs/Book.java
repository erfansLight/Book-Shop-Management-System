package org.example.bs;

import java.util.Date;

public class Book {
    private int id;
    private String ID;
    private String Name;
    private String Description;
    private Double price;
    private String type;
    private String Author;
    private Date date;

    public void setId(int id) {
        this.id = id;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    public void setDate(Date date) {
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

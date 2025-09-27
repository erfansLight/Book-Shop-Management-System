package org.example.bs;

import java.util.Date;

public class Wishlist {

    private int id;
    private String bookID;
    private String name;
    private String description;
    private Double price;
    private String type;
    private String author;
    private Date date;

    public Wishlist(int id, String bookID, String name, String type, String description,
                    Double price, String author, Date date) {
        this.id = id;
        this.bookID = bookID;
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
        this.author = author;
        this.date = date;
    }

    public int getId() { return id; }
    public String getBookID() { return bookID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public String getType() { return type; }
    public String getAuthor() { return author; }
    public Date getDate() { return date; }
}

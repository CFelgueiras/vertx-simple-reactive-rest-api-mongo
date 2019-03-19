package model;

import io.vertx.core.json.JsonObject;

public class Book {

    private int id;
    private String author;
    private String title;

    public Book() {
    }

    public Book(JsonObject jsonObject) {
        this.id = jsonObject.getInteger("id");
        this.author = jsonObject.getString("author");
        this.title = jsonObject.getString("title");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

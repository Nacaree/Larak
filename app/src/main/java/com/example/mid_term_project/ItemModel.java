package com.example.mid_term_project;

public class ItemModel {
    private String name;
    private int image;
    private String description;

    // Constructor with description
    public ItemModel(String name, int image, String description) {
        this.name = name;
        this.image = image;
        this.description = description;
    }

    // Constructor without description (for backward compatibility)
    public ItemModel(String name, int image) {
        this.name = name;
        this.image = image;
        this.description = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
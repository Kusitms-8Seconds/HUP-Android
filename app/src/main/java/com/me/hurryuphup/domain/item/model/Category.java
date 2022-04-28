package com.me.hurryuphup.domain.item.model;

public class Category {
    boolean selected;
    int image;
    String category;

    public Category(boolean selected, int image, String category) {
        this.selected = selected;
        this.image = image;
        this.category = category;
    }
    public String getCategory() {return category;}
    public int getImage() {return image;}
    public boolean getSelected() {return selected;}
    public void setCategory(String category) { this.category = category; }
    public void setImage(int image) {this.image = image;}
    public void setSelected(boolean selected) {this.selected = selected;}
}

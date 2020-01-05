package com.example.sofra.data.local.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    int id;
    public int item_id;
    public int restaurant_id;
    public String client_name;
    String note;
    String photo;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    String item_name;

    public OrderItem() {
    }

    double  price;
    public Integer quantity;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItem(int item_id, int restaurant_id, String client_name, String photo, double price, String note ,
                     int quantity , String item_name) {
        this.item_id = item_id;
        this.restaurant_id = restaurant_id;
        this.client_name = client_name;
        this.price = price;
        this.quantity = quantity;
        this.note=note;
        this.photo=photo;
        this.item_name=item_name;
    }
}

package com.example.sofra.helper;

public class ResturantDataSave {
    public  String resturant_name;
    public  String email;
    public String delivery_time;
    public int cityid;
    public int regionid;
    public  String password;
    public  String password_confirmation;
    public String minimum_order;
    public String deliver_cost;

    public ResturantDataSave(String resturant_name, String email,
                             String delivery_time, int cityid,
                             int regionid, String password,
                             String password_confirmation, String minimum_order,
                             String deliver_cost) {
        this.resturant_name = resturant_name;
        this.email = email;
        this.delivery_time = delivery_time;
        this.cityid = cityid;
        this.regionid = regionid;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.minimum_order = minimum_order;
        this.deliver_cost = deliver_cost;
    }
}

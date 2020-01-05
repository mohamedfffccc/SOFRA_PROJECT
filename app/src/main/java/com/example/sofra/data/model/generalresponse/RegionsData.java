
package com.example.sofra.data.model.generalresponse;

import com.example.sofra.data.model.restaurants.Restaurant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegionsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("city")
    @Expose
    private RegionsData city;
    @SerializedName("code")
    @Expose
    private Integer code;
    private Restaurant data;




    public Restaurant getData() {
        return data;
    }

    public void setData(Restaurant data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public RegionsData(int id, String name) {
        this.id=id;
        this.name=name;


    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public RegionsData getCity() {
        return city;
    }

    public void setCity(RegionsData city) {
        this.city = city;
    }

}

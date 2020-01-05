
package com.example.sofra.data.model.generalresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public RegionsPagination getData() {
        return data;
    }

    public void setData(RegionsPagination data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose


    private RegionsPagination data;
    private RegionsData city;


    public RegionsData getCity() {
        return city;
    }

    public void setCity(RegionsData city) {
        this.city = city;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



}

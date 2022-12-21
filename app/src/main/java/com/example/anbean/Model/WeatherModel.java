package com.example.anbean.Model;

public class WeatherModel {
    private String location;
    private String temp;
    private String w_icon;

    public WeatherModel(String location,String temp, String w_icon) {
        this.location=location;
        this.temp = temp;
        this.w_icon = w_icon;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getW_icon() {
        return w_icon;
    }

    public void setW_icon(String w_icon) {
        this.w_icon = w_icon;
    }
}

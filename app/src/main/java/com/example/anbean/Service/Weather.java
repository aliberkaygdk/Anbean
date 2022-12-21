package com.example.anbean.Service;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("main")
    String wMain;

    public String getwMain() {
        return wMain;
    }

    public void setwMain(String wMain) {
        this.wMain = wMain;
    }
}

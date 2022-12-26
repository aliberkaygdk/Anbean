package com.aliberkaygediko.anbean.Service;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("main")
    String wMain;

    public String getwMain() {
        return wMain;
    }

    @SerializedName("description")
    String wDesc;

    public String getwDesc() {
        return wDesc;
    }




}

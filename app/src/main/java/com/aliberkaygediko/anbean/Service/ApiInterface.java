package com.aliberkaygediko.anbean.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("weather?appid=2d1e5b17d20a604d001b0e1715ad5427&units=metric")
    Call<Example> getWeatherData(@Query("q")String name);
}

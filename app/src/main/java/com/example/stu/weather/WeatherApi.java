package com.example.stu.weather;


import com.example.stu.weather.Weather;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApi {

    String base_url = "http://api.openweathermap.org/";

    @GET("data/2.5/weather")
    Call<Weather> getJson(@Query("q") String city, @Query("APPID") String apiKey);

}

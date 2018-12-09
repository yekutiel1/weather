
package com.example.stu.weather; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    @Expose
    private Float temp;
    @SerializedName("pressure")
    @Expose
    private Integer pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("temp_min")
    @Expose
    private Float temp_min;
    @SerializedName("temp_max")
    @Expose
    private Float temp_max;

    public Float getTemp() {
        return temp;
    }


    public Integer getPressure() {
        return pressure;
    }


    public Integer getHumidity() {
        return humidity;
    }


    public Float getTemp_min() {
        return temp_min;
    }


    public Float getTemp_max() {
        return temp_max;
    }


}

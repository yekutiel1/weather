
package com.example.stu.weather; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    @Expose
    private Float speed;
    @SerializedName("deg")
    @Expose
    private Integer deg;

    public Float getSpeed() {
        return speed;
    }


    public Integer getDeg() {
        return deg;
    }


}

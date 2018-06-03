package app.wheather.com.newweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/5/29.
 */

public class AQI {
    @SerializedName("city")
    public AQICity aqiCity;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}

package app.wheather.com.newweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/5/29.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherTd;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updataTime;



    }



}

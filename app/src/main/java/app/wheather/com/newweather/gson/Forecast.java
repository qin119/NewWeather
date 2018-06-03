package app.wheather.com.newweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/5/29.
 */

public class Forecast {
    public String date;
    public Cond cond;
    public Tmp tmp;

    public class Cond {
        @SerializedName("txt_d")
        public String info;
    }

    public class Tmp {
        public String max;
        public String min;
    }
}

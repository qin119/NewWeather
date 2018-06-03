package app.wheather.com.newweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/4/18.
 */

public class County extends DataSupport {

    public int id;
    public String countyName;
    public String weatherId;
    public int cityId;


    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCountyName() {
        return countyName;
    }

    public void setCityName(String cityName) {
        this.countyName = cityName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }


}

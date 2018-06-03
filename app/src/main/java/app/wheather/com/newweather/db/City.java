package app.wheather.com.newweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/4/18.
 */

public class City extends DataSupport {
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String cityName;
    public int cityCode;
    public int provinceId;
}

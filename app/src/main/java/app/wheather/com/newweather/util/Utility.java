package app.wheather.com.newweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.wheather.com.newweather.db.City;
import app.wheather.com.newweather.db.County;
import app.wheather.com.newweather.db.Province;
import app.wheather.com.newweather.gson.Weather;

/**
 * Created by Administrator on 2018/4/18.
 * 解析和出来json数据
 */

public class Utility {

    /*解析和出来服务器返回的省级数据*/
    public static boolean handleProvinceResponse(String response) {

        LogUtil.i("qsy", "Utility response :" + response);
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*解析和出来服务器返回的市级的数据*/
    public static boolean handleCityResponse(String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {
            try {

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*解析和出来服务器返回的县级数据*/
    public static boolean handleCountyResponse(String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {

            try {
                JSONArray allCountise = new JSONArray(response);
                for (int i = 0; i < allCountise.length(); i++) {
                    JSONObject jsonObject = allCountise.getJSONObject(i);
                    County county = new County();
                    county.setCityName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(cityId);
                    LogUtil.i("qsy","cityId :" + cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    //将返回的json解析到weather实体类中
    public static Weather handleWeatherResponse(String response) {

        LogUtil.i("qsy","handleWeatherResponse.response :" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}


package app.wheather.com.newweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import app.wheather.com.newweather.gson.Forecast;
import app.wheather.com.newweather.gson.Weather;
import app.wheather.com.newweather.util.HttpUtil;
import app.wheather.com.newweather.util.LogUtil;
import app.wheather.com.newweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/30.
 */

public class WeatherActivity extends AppCompatActivity {
    //    @BindView(R.id.title_text)
    TextView titleText;
    //    @BindView(R.id.time_updata_text)
    TextView timeUpdataText;
    //    @BindView(R.id.degree_text)
    TextView degreeText;
    //    @BindView(R.id.weather_info_text)
    TextView weatherInfoText;
    //    @BindView(R.id.degree_text1)
    TextView degreetext1;
    //    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;
    //    @BindView(R.id.aqi_text)
    TextView aqiText;
    //    @BindView(R.id.pm25_text)
    TextView pm25Text;
    //    @BindView(R.id.comfor_text)
    TextView comforText;
    //    @BindView(R.id.car_wash_text)
    TextView carWashText;
    //    @BindView(R.id.sport_text)
    TextView sportText;
    //    @BindView(R.id.weather_layout)
    ScrollView weatherLayout;
    //    @BindView(R.id.data_text)
    TextView dataText;
    //    @BindView(R.id.info_text)
    TextView infoText;
    //    @BindView(R.id.min_text)
    TextView minText;
    //    @BindView(R.id.max_text)
    TextView maxText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
//        ButterKnife.bind(this);
        initData();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherInfo = sharedPreferences.getString("weather", null);
        //有缓存的时候直接解析天气数据
        if (weatherInfo != null) {
            Weather weather = Utility.handleWeatherResponse(weatherInfo);
            showWeatherInfo(weather);
        } else {
            //无缓存的时候去服务器查询数据
            String weatherId = getIntent().getStringExtra("weather_id");
//            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    public void initData() {
        titleText = findViewById(R.id.title_text);
        timeUpdataText = findViewById(R.id.time_updata_text);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        degreetext1 = findViewById(R.id.degree_text1);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comforText = findViewById(R.id.comfor_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        weatherLayout = findViewById(R.id.weather_layout);

    }

    public void requestWeather(final String weatherId) {

        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "onFailure获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText = response.body().string();
                LogUtil.i("qsy", "response:" + response + "call :" + call);
                final Weather weather = Utility.handleWeatherResponse(responseText);
                LogUtil.i("qsy", "weather:" + weather);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            //显示天气信息
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //显示天气数据
    public void showWeatherInfo(Weather weather) {

        LogUtil.i("qsy", "showWeatherInfo.weather :" + weather);
        String cityName = weather.basic.cityName;
        LogUtil.i("qsy", "showWeatherInfo.cityName :" + cityName);
        String updataTime = weather.basic.update.updataTime.split(" ")[1];
        String degree = weather.now.tmp + "℃";
        String weatherInfo = weather.now.cond.txt;
        titleText.setText(cityName);
        timeUpdataText.setText(updataTime);
        degreetext1.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();//从ViewGroup中移除所有子视图
        for (Forecast forecast : weather.forecastsList) {//for循环处理未来几天的天气预报信息
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            dataText = view.findViewById(R.id.data_text);
            infoText = view.findViewById(R.id.info_text);
            minText = view.findViewById(R.id.min_text);
            maxText = view.findViewById(R.id.max_text);
            dataText.setText(forecast.date);
            infoText.setText(forecast.cond.info);
            maxText.setText(forecast.tmp.max);
            minText.setText(forecast.tmp.min);
            forecastLayout.addView(view);

        }
        if (weather.aqi != null) {
//            LogUtil.i("qsy", "weather.aqi.aqiCity.aqi :" + weather.aqi.aqiCity.aqi);
            aqiText.setText(weather.aqi.aqiCity.aqi);
            pm25Text.setText(weather.aqi.aqiCity.pm25);

        }

        String comfor = "舒适度: " + weather.suggestion.comfort.info;
        String carwash = "洗车指数: " + weather.suggestion.carWash.info;
        String sport = "运动建议: " + weather.suggestion.sport.info;
        comforText.setText(comfor);
        carWashText.setText(carwash);
        sportText.setText(sport);
        forecastLayout.setVisibility(View.VISIBLE);
    }

}

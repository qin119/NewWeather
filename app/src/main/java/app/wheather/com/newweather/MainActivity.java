package app.wheather.com.newweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.wheather.com.newweather.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.i("qsy","MainActivity");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sharedPreferences.getString("weather",null) != null){
            Intent intent = new Intent(MainActivity.this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

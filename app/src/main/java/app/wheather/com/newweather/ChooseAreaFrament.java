package app.wheather.com.newweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.wheather.com.newweather.db.City;
import app.wheather.com.newweather.db.County;
import app.wheather.com.newweather.db.Province;
import app.wheather.com.newweather.util.HttpUtil;
import app.wheather.com.newweather.util.LogUtil;
import app.wheather.com.newweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/25.
 */

public class ChooseAreaFrament extends Fragment {

    public final int LEVEL_PROVINCE = 0;
    public final int LEVEL_CITY = 1;
    public final int LEVEL_COUNTY = 2;
    public TextView titleText;
    public Button backButton;
    public ListView listView;
    public ArrayAdapter<String> adapter;
    public List<String> dataList = new ArrayList<>();
    public ProgressDialog progressDialog;

    /*省列表*/
    private List<Province> provinceList;
    /*市列表*/
    private List<City> cityList;
    /*縣列表*/
    private List<County> countyList;
    /*選中的省份*/
    private Province selectedProvice;
    /*選中的城市*/
    private City seletedCity;
    /*當前選中的级别*/
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        LogUtil.i("qsy1", "dataList :" + dataList);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvice = provinceList.get(position);
                    queryCity();
                } else if (currentLevel == LEVEL_CITY) {
                    seletedCity = cityList.get(position);
                    queryCounty();
                }else if (currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        LogUtil.i("qsy", "onActivityCreated");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    //如果是在县级列表则返回城市列表
                    queryCity();
                } else if (currentLevel == LEVEL_CITY) {
                    //如果是在市级列表则返回省级列表
                    queryProvince();
                }
            }
        });
        //返回到省级列表
        queryProvince();
    }

    /*查询全国所有省，有限从数据库查询，如果没有再从服务器查询*/
    public void queryProvince() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);//隐藏返回按钮
        provinceList = DataSupport.findAll(Province.class);
        LogUtil.i("qsy", "provinceList.size() :" + provinceList.size());
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            //从服务器查询全国省的数据
            queryFromServer(address, "province");
        }
    }

    /*查询省内所有市的数据，有限从数据库查找，如果没有再到服务器上找*/
    public void queryCity() {
        titleText.setText(selectedProvice.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvice.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            LogUtil.i("qsy", "queryCity.dataList :" + dataList);
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvice.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            LogUtil.i("qsy", "provinceCode :" + provinceCode);
            //从服务器查询市级的数据
            queryFromServer(address, "city");
        }
    }

    /*查询选中的市内的所有的县，有限从数据库查，如果没有再从服务器上查询*/
    public void queryCounty() {
        titleText.setText(seletedCity.getCityName());
        LogUtil.i("qsy", "titleText :" + seletedCity.getCityName());
        LogUtil.i("qsy", "cityid :" + String.valueOf(seletedCity.getId()));
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(seletedCity.getId())).find(County.class);
        LogUtil.i("qsy", "countyList :" + countyList);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvice.getProvinceCode();
            int cityCode = seletedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            //从服务器查询选中县级的数据
            LogUtil.i("qsy", "queryCounty address :" + address);
            queryFromServer(address, "county");

        }
    }

    public void queryFromServer(String address, final String type) {
        LogUtil.i("qsy", "queryFromServer address :" + address);
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("qsy", "queryFromServer call :" + call);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //回到主线程
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                LogUtil.i("qsy", "response : " + response);
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvice.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, seletedCity.getId());
                }
                LogUtil.i("qsy", "type :" + type);
                LogUtil.i("qsy", "result :" + result);
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCity();
                            } else if ("county".equals(type)) {
                                queryCounty();
                            }
                        }
                    });
                }
            }
        });


    }

    /*显示进度对话框*/
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /*关闭对话框*/
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

package com.example.stu.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.stu.weather.Weather;

import com.example.stu.weather.Main;
import com.example.stu.weather.Wind;
import com.example.stu.weather.Weather_;
import com.example.stu.weather.Sys;

import java.util.List;

import static com.example.stu.weather.R.color.colorPrimary;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Weather jsonResponse;
    private Main main;
    private Wind wind;
    private List<Weather_> weather;
    private Sys sys;

    private TextView mTempTV, mWindSpeed, mPressure;
    private TextView mWeatherMain, mWeatherDescription, mCityName, mCountry;
    private ProgressBar mProgressBar;
    private Spinner mSpinner;
    private LinearLayout mLinearLayout;
    private Button mLocationBtn;

    private WeatherApi client;

    private String cityName;

    final String API_KEY = "50c311f98aa9eec39c2d36dd0923e444";
    final String[] mCities = new String[]{"Jerusalem", "Bnei Brak", "Tel Aviv", "Zefat", "Haifa"};

    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        setRetrofit();

        setSpinner();

    }

    private void setLocation() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_SHORT).show();

                Log.d("location", location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);

        }
    }

    private void initViews() {
        mLinearLayout = findViewById(R.id.ly_container);
        mProgressBar = findViewById(R.id.progress_bar);
        mSpinner = findViewById(R.id.spinner);
        mLocationBtn = findViewById(R.id.btn_location);
        mLocationBtn.setOnClickListener(this);

        mTempTV = findViewById(R.id.temp);
        mWindSpeed = findViewById(R.id.wind_speed);
        mPressure = findViewById(R.id.pressure);
        mWeatherMain = findViewById(R.id.weather_main);
        mWeatherDescription = findViewById(R.id.weather_description);
        mCityName = findViewById(R.id.city_name);
        mCountry = findViewById(R.id.country);
    }

    private void updateViews() {
        mTempTV.setText("Temperature: " + convertKelvin(main.getTemp()));
        mPressure.setText("Pressure: " + main.getPressure());
        mWindSpeed.setText("Wind speed: " + wind.getSpeed());
        mWeatherMain.setText(weather.get(0).getMain());
        mWeatherDescription.setText(weather.get(0).getDescription());
        mCityName.setText(jsonResponse.getName());
        mCountry.setText(sys.getCountry());
    }

    private float convertKelvin(float tempInK) {
        return tempInK - (float) 273.15;
    }

    private void setObjects() {
        main = jsonResponse.getMain();
        wind = jsonResponse.getWind();
        weather = jsonResponse.getWeather();
        sys = jsonResponse.getSys();
    }

    private void setSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mCities);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void setRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(WeatherApi.base_url)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        client = retrofit.create(WeatherApi.class);
    }

    void weatherRequest() {

        Call<Weather> call = client.getJson(cityName + ",il", API_KEY);

        call.enqueue(new Callback<Weather>() {

            private static final String TAG = "onResponse";

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                Log.d(TAG, "onResponse: " + call.request().url());

                mProgressBar.setVisibility(View.GONE);
                mLinearLayout.setBackgroundColor(getColor(R.color.colorWhite));

                jsonResponse = response.body();

                if (jsonResponse != null) {
                    setObjects();
                    updateViews();
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d(TAG, "onResponse: " + call.request().url());
                mProgressBar.setVisibility(View.GONE);
                mLinearLayout.setBackgroundColor(getColor(R.color.colorWhite));
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mLinearLayout.setBackgroundColor(getColor(R.color.colorGray));
        mProgressBar.setVisibility(View.VISIBLE);
        cityName = mCities[i];
        weatherRequest();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);

            }
        }
    }

    @Override
    public void onClick(View view) {
//        setLocation();
    }
}

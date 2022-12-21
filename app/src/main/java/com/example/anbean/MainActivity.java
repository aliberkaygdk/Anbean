package com.example.anbean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.anbean.Fragments.HomeFragment;
import com.example.anbean.Fragments.NotificationFragment;
import com.example.anbean.Fragments.ProfileFragment;
import com.example.anbean.Fragments.SearchFragment;
import com.example.anbean.Service.ApiClient;
import com.example.anbean.Service.ApiInterface;
import com.example.anbean.Service.Example;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    TextView locationTV, temperature,textweather;
    String cityName;


    AppBarLayout top_bar;
    BottomNavigationView bottom_navigation;
    Fragment selectedfragment = null;
    private FirebaseAuth auth;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        top_bar = findViewById(R.id.top_bar);
        locationTV = findViewById(R.id.location);

        temperature = findViewById(R.id.temp);
        textweather=findViewById(R.id.weatherDetail);


        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {

                String city = hereLocation(location.getLatitude(), location.getLongitude());
                locationTV.setText(city);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
            }
        }

        getWeatherData(locationTV.getText().toString().trim());



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            top_bar.setVisibility(View.VISIBLE);
                            selectedfragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            top_bar.setVisibility(View.GONE);
                            selectedfragment = new SearchFragment();
                            break;
                        case R.id.nav_add:
                            top_bar.setVisibility(View.GONE);
                            selectedfragment = null;

                            startActivity(new Intent(MainActivity.this, PostActivity.class));

                            break;
                        case R.id.nav_heart:
                            top_bar.setVisibility(View.GONE);
                            selectedfragment = new NotificationFragment();
                            break;
                        case R.id.nav_profile:
                            top_bar.setVisibility(View.GONE);
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectedfragment = new ProfileFragment();
                            break;
                    }
                    if (selectedfragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedfragment).commit();
                    }

                    return true;
                }
            };




    public String hereLocation(double lat, double lon) {

        String cityName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if (addresses.size() > 0) {
                for (Address adr : addresses) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        cityName = adr.getLocality();
                        break;
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    LocationManager locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    @SuppressLint("MissingPermission") Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {

                        String city = hereLocation(location.getLatitude(), location.getLongitude());
                        locationTV.setText(city);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Permisson not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    private void getWeatherData(String name){
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        Call<Example>call=apiInterface.getWeatherData(name);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {


                temperature.setText(response.body().getMain().getTemp()+"°C");
                textweather.setText("Weather: "+ response.body().getWeatherList().get(0).getwMain());
                //System.out.println(a);
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

    }

}
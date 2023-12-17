package com.builtin.weatherapp;
/*creator Brian Barnabas Langay
        email brianlangay0@gmail.com*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAcivity extends AppCompatActivity  {

    private TextView cityNameTextView, conditionTextView,temperatureTextView;



    private RelativeLayout main_bc;

    ImageView menu,settings;

    private RecyclerView recyclerView;


    TextView feelsLike_c;
    TextView feelsLike_f;
    TextView temp_f;
    TextView humidiy;
    TextView wind;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_acivity);


        //weather init
        cityNameTextView = findViewById(R.id.city);
        temperatureTextView = findViewById(R.id.degrees);
        conditionTextView = findViewById(R.id.weather_des);

        //weather anim init
        LottieAnimationView sunny = findViewById(R.id.sunny);
        LottieAnimationView clear = findViewById(R.id.clear);
        LottieAnimationView rain = findViewById(R.id.rain);
        LottieAnimationView foggy = findViewById(R.id.foggy);
        LottieAnimationView light_rain = findViewById(R.id.light_rain);
        LottieAnimationView sunrize = findViewById(R.id.sunrize);
        LottieAnimationView snow = findViewById(R.id.snow);
        LottieAnimationView snow_sunny = findViewById(R.id.snow_sunny);
        LottieAnimationView cloudy = findViewById(R.id.cloudy);
        LottieAnimationView partlyShower = findViewById(R.id.party_shower);


        feelsLike_c = findViewById(R.id.feels_c);
        feelsLike_f = findViewById(R.id.feels_f);
        temp_f = findViewById(R.id.temp_f);
        humidiy = findViewById(R.id.humidity);
        wind = findViewById(R.id.wind);


        //inits
        main_bc = findViewById(R.id.main_bc);
        menu = findViewById(R.id.menu);
        settings = findViewById(R.id.settings);





        TextView dateTimeTextView = findViewById(R.id.dateTimeTextView);

        // Get the current date and time
        Date currentDate = new Date();

        // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());

        // Format the current date and time
        String formattedDate = dateFormat.format(currentDate);

        // Set the formatted date as text in the TextView
        dateTimeTextView.setText(formattedDate);



        // status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // Set the status bar to be transparent
            window.setStatusBarColor(Color.TRANSPARENT);

            // Make the content appear behind the status bar
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Change bottom nav color based on app's night mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            int navColor;

            if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                // Night mode
                navColor = ContextCompat.getColor(this, R.color.dark);
            } else {
                // Light or dark mode
                navColor = ContextCompat.getColor(this, R.color.d0);
            }

            window.setNavigationBarColor(navColor);
        }




        WeatherApiClient weatherApiClient = new WeatherApiClient(cityNameTextView,
                conditionTextView,
                temperatureTextView,
                feelsLike_c,
                feelsLike_f,
                temp_f,
                humidiy,
                wind,
                partlyShower,
                cloudy,
                rain,
                sunny,
                foggy,
                light_rain,
                sunrize,
                snow_sunny,
                snow,
                clear
        );



       //weatherApiClient.execute(getStoredCityName());
       // weatherApiClient.execute("dar es salaam");

        // Fetch the stored city name from StringManager
        String cityName0 = CityManager.getCityName(this);
        weatherApiClient.execute(cityName0);



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherAcivity.this,Settings.class );
                startActivity(intent);

            }
        });

        //calling our data fetcher for theme
        StringValueManager stringManager = new StringValueManager(this);

        String theme = "default";
        // To retrieve the current value
        String currentValue = stringManager.getValue();
        if (currentValue == null) {
            theme = "default";
        } else if (currentValue != null) {
            theme = currentValue;
        }
        if (theme.contains("fault")) {
            default_theme();

        } else if (theme.contains("ello")) {
            yellow_theme();
        }
        else if (theme.contains("igh")) {
            night_theme();
        }



    }

   public void default_theme(){
        main_bc.setBackground(getResources().getDrawable(R.drawable.blue_bc));

    }

    public void yellow_theme(){
        main_bc.setBackground(getResources().getDrawable(R.drawable.wbc_dark));
    }
    public void night_theme(){
        main_bc.setBackground(getResources().getDrawable(R.drawable.wbc_light));

    }




}
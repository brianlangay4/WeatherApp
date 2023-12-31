package com.builtin.weatherapp;
/*creator Brian Barnabas Langay
        email brianlangay0@gmail.com*/

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherApiClient extends AsyncTask<String, Void, JSONObject> {

    private TextView cityNameTextView;
    private TextView conditionTextView;
    private TextView temperatureTextView;

    private TextView feelsLike_c;
    private TextView feelsLike_f;
    private TextView temp_f;
    private TextView humidiy;
    private TextView wind;
    private LottieAnimationView partlyShower;
    private LottieAnimationView cloudy;
    private LottieAnimationView rain;
    private LottieAnimationView sunny;
    private LottieAnimationView foggy;
    private LottieAnimationView light_rain;
    private LottieAnimationView sunrize;
    private LottieAnimationView snow_sunny;
    private LottieAnimationView snow;
    private LottieAnimationView clear;

    private String condition;

    // Add the cache and cache-related variables
    private static HashMap<String, CachedWeatherData> weatherCache = new HashMap<>();
    private static final long CACHE_EXPIRATION_TIME_MS = 10 * 60 * 1000; // 10 minutes cache expiration
    private static final int MAX_CACHE_SIZE = 50; // Maximum number of cached entries

    // CachedWeatherData class to hold weather data and its expiration time
    private static class CachedWeatherData {
        JSONObject weatherData;
        long expirationTime;

        CachedWeatherData(JSONObject weatherData, long expirationTime) {
            this.weatherData = weatherData;
            this.expirationTime = expirationTime;
        }
    }

    public WeatherApiClient(TextView cityNameTextView,
                            TextView conditionTextView,
                            TextView temperatureTextView,
                            TextView feelsLike_c,
                            TextView feelsLike_f,
                            TextView temp_f,
                            TextView humidiy,
                            TextView wind,
                            LottieAnimationView partlyShower,
                            LottieAnimationView cloudy,
                            LottieAnimationView rain,
                            LottieAnimationView sunny,
                            LottieAnimationView foggy,
                            LottieAnimationView light_rain,
                            LottieAnimationView sunrize,
                            LottieAnimationView snow_sunny,
                            LottieAnimationView snow,
                            LottieAnimationView clear
    ) {
        this.cityNameTextView = cityNameTextView;
        this.conditionTextView = conditionTextView;
        this.temperatureTextView = temperatureTextView;
        this.feelsLike_c = feelsLike_c;
        this.feelsLike_f = feelsLike_f;
        this.temp_f = temp_f;
        this.humidiy = humidiy;
        this.wind = wind;
        this.partlyShower = partlyShower;
        this.cloudy = cloudy;
        this.rain = rain;
        this.sunny = sunny;
        this.foggy = foggy;
        this.light_rain = light_rain;
        this.sunrize = sunrize;
        this.snow_sunny = snow_sunny;
        this.snow = snow;
        this.clear = clear;

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String cityName = params[0];

        // Check if weather data for the given city is available in the cache and not expired
        if (weatherCache.containsKey(cityName)) {
            CachedWeatherData cachedData = weatherCache.get(cityName);
            if (System.currentTimeMillis() < cachedData.expirationTime) {
                return cachedData.weatherData;
            } else {
                // Remove expired data from the cache
                weatherCache.remove(cityName);
            }
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://weatherapi-com.p.rapidapi.com/current.json?q=" + cityName)
                .get()
                .addHeader("X-RapidAPI-Key", "539376df86msha7c2542b821b5bbp1660b6jsn5c7daa1bde3b")
                .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject weatherData = new JSONObject(responseBody);

            // Add the retrieved data to the cache with expiration time
            long expirationTime = System.currentTimeMillis() + CACHE_EXPIRATION_TIME_MS;
            weatherCache.put(cityName, new CachedWeatherData(weatherData, expirationTime));

            // Manage cache size by removing old entries if the cache exceeds the maximum size
            if (weatherCache.size() > MAX_CACHE_SIZE) {
                long currentTime = System.currentTimeMillis();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    weatherCache.entrySet().removeIf(entry -> entry.getValue().expirationTime < currentTime);
                }
            }

            return weatherData;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCondition() {
        return condition;
    }

    @Override
    protected void onPostExecute(JSONObject json) {

        if (json != null) {
            try {
                JSONObject location = json.getJSONObject("location");
                String cityName = location.getString("name");

                //precise long,lat only can get right data about the location
                String country = location.getString("country");
                String region = location.getString("region");
                String lat = location.getString("lat");
                String lon = location.getString("lon");



                String limitedString = cityName.substring(0, Math.min(cityName.length(), 6));
                cityNameTextView.setText(cityName);
                JSONObject current = json.getJSONObject("current");
                condition = current.getJSONObject("condition").getString("text");
                conditionTextView.setText(condition);


                //more
                JSONObject current2 = json.getJSONObject("current");
                String last_updated = current2.getString("last_updated");

               // String localTime = current2.getString("localtime");
                String temp_c = current2.getString("temp_c");
                String tempr_f = current2.getString("temp_f");
                String humidity = current2.getString("humidity");
                String feelslike_c = current2.getString("feelslike_c");
                String feelslike_f = current2.getString("feelslike_f");
                String wind_kph = current2.getString("wind_kph");



                feelsLike_c.setText(feelslike_c);
                feelsLike_f.setText(feelslike_f);
                temp_f.setText(tempr_f);
                humidiy.setText(humidity);
                wind.setText(wind_kph);







                int tempC = current.getInt("temp_c");
                temperatureTextView.setText(String.valueOf(tempC) + "°");

                System.out.println("Weather APi : current city condition = "+condition);

                if (condition.contains("un")) {
                    //sunrize.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.VISIBLE);
                } else if (condition.contains("ain")) {
                    rain.setVisibility(View.VISIBLE);
                } else if (condition.contains("zzle")) {
                    light_rain.setVisibility(View.VISIBLE);
                } else if (condition.contains("loud")) {
                    cloudy.setVisibility(View.VISIBLE);
                } else if (condition.contains("oggy")) {
                    foggy.setVisibility(View.VISIBLE);
                } else if (condition.contains("lear")) {
                    sunny.setVisibility(View.VISIBLE);
                } else if (condition.contains("vercast")) {
                    cloudy.setVisibility(View.VISIBLE);
                } else if (condition.contains("now")) {
                    snow.setVisibility(View.VISIBLE);
                }else if (condition.contains("Mist")) {
                    foggy.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            temperatureTextView.setVisibility(View.INVISIBLE);
            conditionTextView.setVisibility(View.INVISIBLE);
            cityNameTextView.setText("Failed to fetch weather data.");
        }
    }

}

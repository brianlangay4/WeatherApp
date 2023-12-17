package com.builtin.weatherapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityValidationApiClient extends AsyncTask<String, Void, Boolean> {

    private CityValidationListener listener;

    public CityValidationApiClient(CityValidationListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String cityName = params[0];

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://weatherapi-com.p.rapidapi.com/current.json?q=" + cityName)
                .get()
                .addHeader("X-RapidAPI-Key", "539376df86msha7c2542b821b5bbp1660b6jsn5c7daa1bde3b")
                .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();

            // Assuming the validation logic based on the response
            // Modify this logic based on the actual response from the API
            if (response.isSuccessful()) {
                // Check the response content to determine if the city is valid
                JSONObject jsonResponse = new JSONObject(response.body().string());
                return jsonResponse.has("location") && jsonResponse.getJSONObject("location").getString("name").equalsIgnoreCase(cityName);
            } else {
                return false;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isValid) {
        if (listener != null) {
            listener.onValidationResult(isValid);
        }
    }

    // Interface to notify the listener about the validation result
    public interface CityValidationListener {
        void onValidationResult(boolean isValid);
    }
}

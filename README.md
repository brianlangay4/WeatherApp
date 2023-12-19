# WeatherApp 

##
#BRIAN BARNABAS LANGAY
2322190027
##

[weatherApp.webm](https://github.com/brianlangay4/WeatherApp/assets/67788456/00820071-c2aa-48cc-8180-e404ec3362b2)



## Introduction
WeatherApp is a mobile application designed to provide users with real-time weather information for a specified location. The app utilizes an API call to retrieve weather data and presents it in a user-friendly interface. Additionally, the app features a settings page that allows users to customize the theme.

## Features

### 1. WeatherApiClient Class
The `WeatherApiClient` class is responsible for handling API calls to obtain weather data. It extends `AsyncTask` to perform network operations in the background.

#### Attributes
- `cityNameTextView`: TextView to display the city name.
- `conditionTextView`: TextView to display the weather condition.
- `temperatureTextView`: TextView to display the temperature.
- Other TextViews and LottieAnimationView for additional weather details.

#### Caching
The class implements a caching mechanism to store and retrieve weather data efficiently. Cached data is kept for a specified expiration time to ensure freshness.

#### API Call
The API call is made using the OkHttp library, and the response is processed to extract relevant weather information.

#### onPostExecute
The `onPostExecute` method updates the UI with the retrieved weather data, including the city name, condition, temperature, and additional details. It also handles the visibility of LottieAnimationViews based on the weather condition.

### 2. Settings Page
The app includes a settings page where users can customize the theme. This customization is achieved through the use of a spinner, allowing users to select their preferred theme.

#### String Manager Approach
The app employs a string manager approach to store and retrieve the last selected theme. This ensures that the selected theme persists across app launches.

## Usage

### 1. Weather Activity
To integrate weather data into your app, follow these steps:

1. Instantiate the `WeatherApiClient` class, providing the necessary TextViews and LottieAnimationViews for weather display.

```java
WeatherApiClient weatherApiClient = new WeatherApiClient(
    cityNameTextView,
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
```

2. Execute the API call by calling `execute` on the `WeatherApiClient` instance, passing the city name as a parameter.

```java
weatherApiClient.execute("CityName");
```

### 2. Settings Page
The settings page allows users to select a theme from the spinner. The selected theme will be applied to the Weather Activity.

## layout preview

![weatherApp](https://github.com/brianlangay4/WeatherApp/assets/67788456/d5e761b5-a0cd-4448-ac35-bb625e407879)




## Conclusion
WeatherApp provides a seamless way to integrate weather data into your Android application. The included caching mechanism ensures efficient data retrieval, while the settings page allows users to personalize their app experience.

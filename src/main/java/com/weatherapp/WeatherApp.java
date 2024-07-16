package com.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

public class WeatherApp {
    private static final String API_KEY = "0e0b1b82c5cc9543f13b91ff32266a54";

    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService(API_KEY);
        String weatherDataJson = weatherService.fetchWeatherData(40.7128, -74.0060, "minutely,hourly");
        if (weatherDataJson != null) {
            WeatherData weatherData = weatherService.parseWeatherData(weatherDataJson);
            System.out.println(weatherData);
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }
}


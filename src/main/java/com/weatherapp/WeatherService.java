package com.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

class WeatherService {
    private static final String BASE_URL = "https://api.openweathermap.org/data/3.0/onecall";
    private String apiKey;

    public WeatherService(String apiKey) {
        this.apiKey = apiKey;
    }

    public String buildApiUrl(double lat, double lon, String exclude) {
        return BASE_URL + "?lat=" + lat + "&lon=" + lon + "&exclude=" + exclude + "&appid=" + apiKey;
    }

    public String fetchWeatherData(double lat, double lon, String exclude) {
        try {
            URL url = new URL(buildApiUrl(lat, lon, exclude));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == 401) {
                throw new RuntimeException("HTTP 401 Unauthorized: Check your API key.");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public WeatherData parseWeatherData(String jsonResponse) {
        Gson gson = new Gson();
        WeatherResponse weatherResponse = gson.fromJson(jsonResponse, WeatherResponse.class);
        double temperature = weatherResponse.getCurrent().getTemp();
        String condition = weatherResponse.getCurrent().getWeather().get(0).getMain();
        return new WeatherData(temperature, condition);
    }
}

class WeatherData {
    private double temperature;
    private String condition;

    public WeatherData(double temperature, String condition) {
        this.temperature = temperature;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Temperature: " + temperature + ", Condition: " + condition;
    }
}

class WeatherResponse {
    private Current current;

    public Current getCurrent() {
        return current;
    }

    static class Current {
        private double temp;
        private java.util.List<Weather> weather;

        public double getTemp() {
            return temp;
        }

        public java.util.List<Weather> getWeather() {
            return weather;
        }
    }

    static class Weather {
        private String main;

        public String getMain() {
            return main;
        }
    }
}

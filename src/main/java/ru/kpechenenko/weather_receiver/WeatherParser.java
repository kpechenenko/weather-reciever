package ru.kpechenenko.weather_receiver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

final class WeatherParser {
    private final static String OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final int CONNECTION_TIMEOUT = 1000;
    private final static String kostyasHometownName = "barnaul";
    private final String locationNameForQuery;

    WeatherParser(String locationName) {
        validateLocationName(locationName);
        this.locationNameForQuery = locationName;
    }

    static WeatherParser createWeatherParserForHometown() {
        return new WeatherParser(kostyasHometownName);
    }

    private static String getSecretApiKeyForOpenWeatherService() {
        String absPathToPropertiesFile = "/home/kostya/from-scratch/custom-weather-receiver/src/main/resources/config.properties";
        try (InputStream inputPropertyStream = new FileInputStream(absPathToPropertiesFile)) {
            Properties property = new Properties();
            property.load(inputPropertyStream);
            return property.getProperty("openweather.apikey");
        } catch (IOException e) {
            throw new IllegalStateException("Error: api key for openweathermapmap.org not found.", e);
        }
    }

    String getLocationName() {
        return locationNameForQuery;
    }

    private void validateLocationName(String locationName) {
        if (locationName == null) {
            throw new IllegalArgumentException("Location name must be not null.");
        }
        if (locationName.length() <= 3) {
            throw new IllegalArgumentException("Location name length must be more than 3.");
        }
        if (!locationName.matches("[a-z]{3,}")) {
            throw new IllegalArgumentException("Location name must be contains only lowercase english letters.");
        }
    }

    String createUrlForOpenWeatherServiceAndCurrentLocation() {
        return String.format("%s?units=metric&q=%s&appid=%s",
            WeatherParser.OPEN_WEATHER_URL,
            this.locationName,
            WeatherParser.getSecretApiKeyForOpenWeatherService()
        );
    }

    String getCurrentWeatherAsJsonString() throws IOException {
        URL url = new URL(createUrlForOpenWeatherServiceAndCurrentLocation());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(CONNECTION_TIMEOUT);
        if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new IllegalArgumentException(
                String.format("Weather forecast for %s not found.", this.locationName)
            );
        }
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IllegalStateException("Error creating HTTP connection.");
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder weatherReportRawJson = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                weatherReportRawJson.append(inputLine);
            }
            return weatherReportRawJson.toString();
        }
    }
}

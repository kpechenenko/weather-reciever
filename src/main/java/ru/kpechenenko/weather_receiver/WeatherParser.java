package ru.kpechenenko.weather_receiver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

final class WeatherParser {
    private final static String OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final static int CONNECTION_TIMEOUT_DEFAULT_VALUE = 1000;
    private final int connectionTimeout;
    private final String locationName;

    WeatherParser(String locationName, int connectionTimeout) {
        this.validateLocationName(locationName);
        this.locationName = locationName;
        this.getValidateConnectionTimeout(connectionTimeout);
        this.connectionTimeout = connectionTimeout;
    }

    WeatherParser(String locationName) {
        this(locationName, CONNECTION_TIMEOUT_DEFAULT_VALUE);
    }


    private static String getSecretApiKeyForOpenWeatherService() {
        String absPathToPropertiesFile = "src/main/resources/config.properties";
        try (InputStream inputPropertyStream = new FileInputStream(absPathToPropertiesFile)) {
            Properties property = new Properties();
            property.load(inputPropertyStream);
            return property.getProperty("openweather.apikey");
        } catch (IOException e) {
            throw new IllegalStateException("Error: api key for openweathermapmap.org not found.", e);
        }
    }

    private void getValidateConnectionTimeout(int connectionTimeout) {
        if (connectionTimeout <= 0) {
            throw new IllegalArgumentException("Connection time out must be positive.");
        }
        if (connectionTimeout >= 5000) {
            throw new IllegalArgumentException("Connection time out must be less than 5000 ms.");
        }
    }

    String getLocationName() {
        return this.locationName;
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
        connection.setConnectTimeout(this.connectionTimeout);
        connection.setReadTimeout(this.connectionTimeout);
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

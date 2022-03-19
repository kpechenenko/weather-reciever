package ru.kpechenenko.weather_receiver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WeatherParserTest {
	private WeatherParser parserForHomeLocation;

	@BeforeEach
	void initHomeParser() {
		parserForHomeLocation = WeatherParser.createWeatherParserForHometown();
	}

	@Test
	void createUrlForOpenWeatherServiceTest() {
		String locationName = "novosibirsk";
		WeatherParser parser = new WeatherParser(locationName);
		String expectedUrlWithoutApiKey = "https://api.openweathermap.org/data/2.5/weather?units=metric&q=" + locationName + "&appid=";
		assertTrue(parser.createUrlForOpenWeatherServiceAndCurrentLocation().startsWith(expectedUrlWithoutApiKey));
	}

	@Test
	void weatherParserForHomeNameTest() {
		assertEquals("barnaul", parserForHomeLocation.getLocationName());
	}

	@Test
	void getCurrentWeatherAsJsonStringTest() {
		try {
			String weatherReportJsonString = parserForHomeLocation.getCurrentWeatherAsJsonString();
			assertTrue(weatherReportJsonString.contains("temp"));
			assertTrue(weatherReportJsonString.contains("feels_like"));
			assertTrue(weatherReportJsonString.contains("speed"));
			assertTrue(weatherReportJsonString.contains("humidity"));
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	void wrongInputLocationNameThrowExceptionTest() {
		assertThrows(IllegalArgumentException.class,
			() -> {
				WeatherParser parser = new WeatherParser("zopa");
				parser.getCurrentWeatherAsJsonString();
			}
		);
	}
}
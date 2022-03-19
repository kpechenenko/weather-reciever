package ru.kpechenenko.weather_receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class WeatherReportTest {
	private WeatherReport report;

	@BeforeEach
	void initWeatherReport() {
		try {
			report = new ObjectMapper().readValue(
				"{\"coord\":{\"lon\":83.7636,\"lat\":53.3606},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"base\":\"stations\",\"main\":{\"temp\":3.4,\"feels_like\":-2.23,\"temp_min\":2.7,\"temp_max\":3.4,\"pressure\":1004,\"humidity\":81},\"visibility\":10000,\"wind\":{\"speed\":9,\"deg\":210},\"clouds\":{\"all\":40},\"dt\":1646575686,\"sys\":{\"type\":1,\"id\":8945,\"country\":\"RU\",\"sunrise\":1646528525,\"sunset\":1646568650},\"timezone\":25200,\"id\":1510853,\"name\":\"Barnaul\",\"cod\":200}\n",
				WeatherReport.class
			);
		} catch (JsonProcessingException e) {
			fail();
		}
	}

	@Test
	void getCurrentTemperatureTest() {
		assertEquals(3.4, report.getTemperatureSummary().getCurrentTemperature());
	}
	@Test
	void getFeelsLikeTemperatureTest() {
		assertEquals(-2.23, report.getTemperatureSummary().getFeelsLike());
	}

	@Test
	void getWindSpeedTest() {
		assertEquals(9, report.getWindSummary().getSpeed());
	}

	@Test
	void getHumidityTest() {
		assertEquals(81, report.getTemperatureSummary().getHumidity());
	}

	@Test
	void weatherReportToStringTest() {
		assertEquals("Barnaul, scattered clouds, temp 3.4°, feels like -2.23°, wind speed 9m/s, humidity 81%", report.toString());
	}
}
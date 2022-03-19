package ru.kpechenenko.weather_receiver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
final class WeatherReport {
	@JsonProperty("weather")
	private List<WeatherSummary> weatherSummaries;
	@JsonProperty("main")
	private TemperatureSummary temperatureSummary;
	@JsonProperty("wind")
	private WindSummary windSummary;
	@JsonProperty("name")
	private String locationName;

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append(locationName);
		if (weatherSummaries.size() > 0) {
			out.append(", ");
			out.append(weatherSummaries.get(0).getDescription());
		}
		out.append(", temp ");
		out.append(temperatureSummary.getCurrentTemperature());
		out.append("°, feels like ");
		out.append(temperatureSummary.getFeelsLike());
		out.append("°, wind speed ");
		out.append(windSummary.getSpeed());
		out.append("m/s");
		out.append(", humidity ");
		out.append(temperatureSummary.getHumidity().intValue());
		out.append("%");
		return out.toString();
	}

	void printReportToStandardOutput() {
		System.out.println(this);
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class TemperatureSummary {
		@JsonProperty("temp")
		private Double currentTemperature;
		@JsonProperty("feels_like")
		private Double feelsLike;
		private Double humidity;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class WindSummary {
		private Integer speed;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class WeatherSummary {
		private String description;
	}
}

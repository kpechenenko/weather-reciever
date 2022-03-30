package ru.kpechenenko.weather_receiver;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.MalformedURLException;
import java.net.ProtocolException;

public class Client {
	public static void main(String[] args) {
		try {
			WeatherReport weatherReport = new ObjectMapper().readValue(
				new WeatherParser("barnaul").getCurrentWeatherAsJsonString(),
				WeatherReport.class
			);
			weatherReport.printReportToStandardOutput();
		} catch (MalformedURLException e) {
			System.out.println("Error creating URL.");
		} catch (ProtocolException e) {
			System.out.println("Error in underlying protocol.");
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("Error, try later.");
		}
	}
}

weather-receiver
===

General info
---

This is a simple application to get a weather forecast for my hometown.

I have used [https://openweathermap.org/](https://openweathermap.org/)

How I use it
---
```bash
$ getweather
Barnaul, clear sky, temp 15.4°, feels like 13.94°, wind speed 3m/s, humidity 36%
```

How you can use it
---

1. Get your apikey [https://openweathermap.org/appid](https://openweathermap.org/appid)
2. Insert your api key to `src/main/resourses/config.properties`, field `openweather.apikey`.
3. Make sure your city exists on the [https://openweathermap.org/](https://openweathermap.org/)
4. Insert your location name to `src/main/resourses/config.properties`, field `location.name`.
5. Run and execute.

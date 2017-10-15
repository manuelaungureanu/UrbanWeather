package com.chefless.ela.urbanweather.data;

import java.io.Serializable;

/**
 * Created by ela on 11/10/2017.
 */

public class CityForecast implements Serializable {

    public CityForecast(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;
    private Weather[] weather;
    private MainWeatherIndicators main;
    private Wind wind;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public MainWeatherIndicators getMain() {
        return main;
    }

    public void setMain(MainWeatherIndicators main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public class Weather implements Serializable{
        String main;
        String description;

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class MainWeatherIndicators implements Serializable {
        float temp;
        String pressure;
        String humidity;
        float temp_min;
        float temp_max;

        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public float getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(float temp_min) {
            this.temp_min = temp_min;
        }

        public float getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(float temp_max) {
            this.temp_max = temp_max;
        }
    }

    public class Wind{
        float speed;
        float deg;

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public float getDeg() {
            return deg;
        }

        public void setDeg(float deg) {
            this.deg = deg;
        }
    }
}

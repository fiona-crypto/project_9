package org.example.project_9.backend;

public class test {
    public static void main(String[] args){
        WeatherStat weatherStat = ApiRequests.getWeatherStats("Vienna", "metric");
        System.out.println(weatherStat);
    }
}

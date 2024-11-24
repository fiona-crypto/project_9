package org.example.project_9.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherInfo {
    private String mainCondition;
    private String description;

    @Override
    public String toString() {
        return "WeatherInfo{" +
                " mainCondition='" + mainCondition + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}

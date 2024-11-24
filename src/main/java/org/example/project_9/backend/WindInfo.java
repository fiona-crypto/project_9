package org.example.project_9.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class WindInfo {
    private double speed;
    private double direction;

    @Override
    public String toString() {
        return "WindInfo{" +
                " speed=" + speed +
                ", direction=" + direction +
                '}';
    }
}

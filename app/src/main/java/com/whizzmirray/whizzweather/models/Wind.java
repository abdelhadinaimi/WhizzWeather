package com.whizzmirray.whizzweather.models;

/**
 * Created by Whizz Mirray on 02/24/2018.
 */

public class Wind {
    private double speed;
    private double deg;

    public Wind(){
        speed = 0;
        deg = 0;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}

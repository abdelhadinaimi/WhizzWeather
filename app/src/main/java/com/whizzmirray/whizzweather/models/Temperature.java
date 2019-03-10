package com.whizzmirray.whizzweather.models;

/**
 * Created by Whizz Mirray on 02/24/2018.
 */

public class Temperature {
    private double tempMin;
    private double tempMax;
    private double tempCurrent;

    public double getTempCurrent() {
        return tempCurrent;
    }

    public void setTempCurrent(double tempCurrent) {
        this.tempCurrent = tempCurrent;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public static double toFahrenheit(double temp){
        return temp*1.8 + 32;
    }
    @Override
    public String toString() {
        return String.valueOf(getTempCurrent());
    }
}

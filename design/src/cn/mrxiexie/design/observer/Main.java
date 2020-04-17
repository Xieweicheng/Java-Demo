package cn.mrxiexie.design.observer;

import cn.mrxiexie.design.observer.observable.impl.WeatherData;
import cn.mrxiexie.design.observer.observer.impl.CurrentConditionsDisplay;

/**
 * @author mrxiexie
 * @date 9/12/19 4:47 PM
 */
public class Main {

    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionsDisplay display = new CurrentConditionsDisplay(weatherData);
        weatherData.setMeasurementsChanged(1.0f, 1.0f, 1.0f);
    }
}

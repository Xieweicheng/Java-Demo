package cn.mrxiexie.design.observer.observer.impl;

import cn.mrxiexie.design.observer.DisplayElement;
import cn.mrxiexie.design.observer.observable.Observable;
import cn.mrxiexie.design.observer.observable.impl.WeatherData;
import cn.mrxiexie.design.observer.observer.Observer;

/**
 * @author mrxiexie
 * @date 9/12/19 4:09 PM
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {

    private Observable observable;

    private float temperature;
    private float humidity;

    public CurrentConditionsDisplay(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    @Override
    public void display() {
        System.out.println("Current conditions: " + temperature + "F degrees and " + humidity + "% humidity");
    }

    @Override
    public void update(Observable observable, Object object) {
        if (observable instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) observable;
            this.temperature = weatherData.getTemperature();
            this.humidity = weatherData.getHumidity();
            display();
        }
    }
}

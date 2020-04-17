package cn.mrxiexie.design.observer.observable.impl;

import cn.mrxiexie.design.observer.observable.Observable;
import cn.mrxiexie.design.observer.observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrxiexie
 * @date 9/12/19 3:32 PM
 */
public class WeatherData implements Observable {

    private float temperature;
    private float humidity;
    private float pressure;

    private boolean changed;

    private List<Observer> observers;

    public WeatherData() {
        observers = new ArrayList<>();
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setMeasurementsChanged(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        setChanged(true);
        measurementsChanged();
    }

    public void measurementsChanged() {
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object object) {
        if (isChanged()) {
            for (Observer observer : observers) {
                observer.update(this, object);
            }
            setChanged(false);
        }
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    private boolean isChanged() {
        return changed;
    }

    private void setChanged(boolean changed) {
        this.changed = changed;
    }
}

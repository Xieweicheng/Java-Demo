package cn.mrxiexie.design.observer.observable;

import cn.mrxiexie.design.observer.observer.Observer;

/**
 * 可观察者
 *
 * @author mrxiexie
 * @date 9/12/19 3:28 PM
 */
public interface Observable {

    /**
     * 添加观察者
     *
     * @param observer 观察者
     */
    void addObserver(Observer observer);

    /**
     * 删除观察者
     *
     * @param observer 观察者
     */
    void deleteObserver(Observer observer);

    /**
     * 通知所有观察者
     */
    void notifyObservers(Object object);
}

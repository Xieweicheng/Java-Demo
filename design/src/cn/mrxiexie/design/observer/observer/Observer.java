package cn.mrxiexie.design.observer.observer;

import cn.mrxiexie.design.observer.observable.Observable;

/**
 * 观察者
 *
 * @author mrxiexie
 * @date 9/12/19 3:30 PM
 */
public interface Observer {

    /**
     * 更新
     */
    void update(Observable observable, Object object);
}

package cn.mrxiexie.design.factory.abstractfactory;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.abstractfactory.pizza.Pizza;

/**
 * 工厂方法
 *
 * @author mrxiexie
 * @date 19-9-15 下午10:31
 */
public abstract class PizzaStore {

    public final Pizza orderPizza(PizzaEnum pizzaEnum) {
        Pizza pizza = createPizza(pizzaEnum);
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }

    protected abstract Pizza createPizza(PizzaEnum pizzaEnum);
}

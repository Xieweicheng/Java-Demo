package cn.mrxiexie.design.factory.simplefactory;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.simplefactory.pizza.Pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:08
 */
public class PizzaStore {

    SimplePizzaFactory simplePizzaFactory;

    public PizzaStore(SimplePizzaFactory simplePizzaFactory) {
        this.simplePizzaFactory = simplePizzaFactory;
    }

    public Pizza orderPizza(PizzaEnum pizzaEnum) {
        Pizza pizza = simplePizzaFactory.createPizza(pizzaEnum);
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }
}

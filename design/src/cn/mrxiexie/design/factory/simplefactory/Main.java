package cn.mrxiexie.design.factory.simplefactory;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.simplefactory.pizza.Pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:12
 */
public class Main {

    public static void main(String[] args) {
        PizzaStore pizzaStore = new PizzaStore(new SimplePizzaFactory());
        Pizza pizza = pizzaStore.orderPizza(PizzaEnum.CHEESE);
    }
}

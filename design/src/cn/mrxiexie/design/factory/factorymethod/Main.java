package cn.mrxiexie.design.factory.factorymethod;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.factorymethod.pizza.Pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:39
 */
public class Main {

    public static void main(String[] args) {
        Pizza pizza = new NYPizzaStore().orderPizza(PizzaEnum.CHEESE);
        pizza = new ChicagoPizzaStore().orderPizza(PizzaEnum.CHEESE);
    }
}

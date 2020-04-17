package cn.mrxiexie.design.factory.abstractfactory;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.abstractfactory.pizza.Pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午11:06
 */
public class Main {

    public static void main(String[] args) {
        Pizza pizza = new NYPizzaStore().orderPizza(PizzaEnum.CHEESE);
        System.out.println(pizza);
    }
}

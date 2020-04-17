package cn.mrxiexie.design.factory.simplefactory;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.simplefactory.pizza.*;

/**
 * 简单工厂
 *
 * @author mrxiexie
 * @date 19-9-15 下午9:45
 */
public class SimplePizzaFactory {

    public Pizza createPizza(PizzaEnum pizzaEnum) {
        Pizza pizza = null;
        switch (pizzaEnum) {
            case CHEESE:
                pizza = new CheesePizza();
                break;
            case CLAM:
                pizza = new ClamPizza();
                break;
            case VEGGIE:
                pizza = new VeggiePizza();
                break;
            case PEPPERONI:
                pizza = new PepperoniPizza();
                break;
            default:
        }
        return pizza;
    }
}

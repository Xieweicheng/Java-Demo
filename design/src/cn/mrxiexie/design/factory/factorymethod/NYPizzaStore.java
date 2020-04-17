package cn.mrxiexie.design.factory.factorymethod;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.factorymethod.pizza.*;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:32
 */
public class NYPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(PizzaEnum pizzaEnum) {
        Pizza pizza = null;
        switch (pizzaEnum) {
            case CHEESE:
                pizza = new NYStyleCheesePizza();
                break;
            case CLAM:
                pizza = new NYStyleClamPizza();
                break;
            case VEGGIE:
                pizza = new NYStyleVeggiePizza();
                break;
            case PEPPERONI:
                pizza = new NYStylePepperoniPizza();
                break;
            default:
        }
        return pizza;
    }
}

package cn.mrxiexie.design.factory.factorymethod;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.factorymethod.pizza.*;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:42
 */
public class ChicagoPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(PizzaEnum pizzaEnum) {
        Pizza pizza = null;
        switch (pizzaEnum) {
            case CHEESE:
                pizza = new ChicagoStyleCheesePizza();
                break;
            case CLAM:
                pizza = new ChicagoStyleClamPizza();
                break;
            case VEGGIE:
                pizza = new ChicagoStyleVeggiePizza();
                break;
            case PEPPERONI:
                pizza = new ChicagoStylePepperoniPizza();
                break;
            default:
        }
        return pizza;
    }
}

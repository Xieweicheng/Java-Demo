package cn.mrxiexie.design.factory.abstractfactory;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.ChicagoPizzaIngredientFactory;
import cn.mrxiexie.design.factory.abstractfactory.pizza.*;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:42
 */
public class ChicagoPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(PizzaEnum pizzaEnum) {
        ChicagoPizzaIngredientFactory nyPizzaIngredientFactory = new ChicagoPizzaIngredientFactory();
        Pizza pizza = null;
        switch (pizzaEnum) {
            case CHEESE:
                pizza = new CheesePizza(nyPizzaIngredientFactory);
                break;
            case CLAM:
                pizza = new ClamPizza(nyPizzaIngredientFactory);
                break;
            case VEGGIE:
                pizza = new VeggiePizza(nyPizzaIngredientFactory);
                break;
            case PEPPERONI:
                pizza = new PepperoniPizza(nyPizzaIngredientFactory);
                break;
            default:
        }
        return pizza;
    }
}

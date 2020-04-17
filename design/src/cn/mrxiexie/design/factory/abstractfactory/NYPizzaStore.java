package cn.mrxiexie.design.factory.abstractfactory;

import cn.mrxiexie.design.factory.PizzaEnum;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.NYPizzaIngredientFactory;
import cn.mrxiexie.design.factory.abstractfactory.pizza.*;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:32
 */
public class NYPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(PizzaEnum pizzaEnum) {
        NYPizzaIngredientFactory nyPizzaIngredientFactory = new NYPizzaIngredientFactory();
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

package cn.mrxiexie.design.factory.abstractfactory.pizza;

import cn.mrxiexie.design.factory.abstractfactory.ingredient.PizzaIngredientFactory;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:34
 */
public class PepperoniPizza extends Pizza {

    private PizzaIngredientFactory pizzaIngredientFactory;

    public PepperoniPizza(PizzaIngredientFactory pizzaIngredientFactory) {
        this.pizzaIngredientFactory = pizzaIngredientFactory;
    }

    @Override
    public void prepare() {
        System.out.println("PepperoniPizza.prepare");
        dough = pizzaIngredientFactory.createDough();
        sauce = pizzaIngredientFactory.createSauce();
        cheese = pizzaIngredientFactory.createCheese();
        clams = pizzaIngredientFactory.createClams();
        veggies = pizzaIngredientFactory.createVeggies();
        pepperoni = pizzaIngredientFactory.createPepperoni();
    }
}

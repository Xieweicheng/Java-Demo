package cn.mrxiexie.design.factory.abstractfactory.ingredient;

import cn.mrxiexie.design.factory.abstractfactory.ingredient.pepperoni.Pepperoni;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.cheese.Cheese;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.clams.Clams;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.dough.Dough;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.sauce.Sauce;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.veggies.Veggies;

/**
 * @author mrxiexie
 * @date 19-9-15 下午11:17
 */
public interface PizzaIngredientFactory {

    Dough createDough();
    Sauce createSauce();
    Cheese createCheese();
    Veggies[] createVeggies();
    Pepperoni createPepperoni();
    Clams createClams();
}

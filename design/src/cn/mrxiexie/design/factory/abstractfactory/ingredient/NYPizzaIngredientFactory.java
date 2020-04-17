package cn.mrxiexie.design.factory.abstractfactory.ingredient;

import cn.mrxiexie.design.factory.abstractfactory.ingredient.cheese.Cheese;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.cheese.ReggianoCheese;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.clams.Clams;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.clams.FreshClams;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.dough.Dough;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.dough.ThinCrustDough;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.pepperoni.Pepperoni;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.pepperoni.SlicedPepperoni;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.sauce.MarinaraSauce;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.sauce.Sauce;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.veggies.*;

/**
 * @author mrxiexie
 * @date 19-9-15 下午11:29
 */
public class NYPizzaIngredientFactory implements PizzaIngredientFactory {

    @Override
    public Dough createDough() {
        return new ThinCrustDough();
    }

    @Override
    public Sauce createSauce() {
        return new MarinaraSauce();
    }

    @Override
    public Cheese createCheese() {
        return new ReggianoCheese();
    }

    @Override
    public Veggies[] createVeggies() {
        return new Veggies[]{new Garlic(), new Onion(), new Mushroom(), new RedPepper()};
    }

    @Override
    public Pepperoni createPepperoni() {
        return new SlicedPepperoni();
    }

    @Override
    public Clams createClams() {
        return new FreshClams();
    }
}

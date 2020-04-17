package cn.mrxiexie.design.factory.abstractfactory.pizza;

import cn.mrxiexie.design.factory.abstractfactory.ingredient.pepperoni.Pepperoni;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.cheese.Cheese;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.clams.Clams;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.dough.Dough;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.sauce.Sauce;
import cn.mrxiexie.design.factory.abstractfactory.ingredient.veggies.Veggies;

import java.util.Arrays;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:33
 */
public abstract class Pizza {

    String name;
    Dough dough;
    Sauce sauce;
    Veggies veggies[];
    Cheese cheese;
    Pepperoni pepperoni;
    Clams clams;

    public abstract void prepare();

    public void bake() {
        System.out.println("Pizza.bake");
    }

    public void cut() {
        System.out.println("Pizza.cut");
    }

    public void box() {
        System.out.println("Pizza.box");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "name='" + name + '\'' +
                ", dough=" + dough +
                ", sauce=" + sauce +
                ", veggies=" + Arrays.toString(veggies) +
                ", cheese=" + cheese +
                ", pepperoni=" + pepperoni +
                ", clams=" + clams +
                '}';
    }
}

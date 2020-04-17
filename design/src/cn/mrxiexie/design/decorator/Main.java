package cn.mrxiexie.design.decorator;

/**
 * @author mrxiexie
 * @date 19-9-15 下午2:51
 */
public class Main {

    public static void main(String[] args) {
        double cost = new Espresso().cost();
        System.out.println(cost);

        Beverage beverage = new Soy(new Mocha(new Whip(new Espresso())));
        System.out.println(beverage.cost());
        System.out.println(beverage.getDescription());
    }
}

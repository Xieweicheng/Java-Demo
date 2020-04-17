package cn.mrxiexie.design.factory.simplefactory.pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:06
 */
public class PepperoniPizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("PepperoniPizza.prepare");
    }
}

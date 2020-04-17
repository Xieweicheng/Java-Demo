package cn.mrxiexie.design.factory.simplefactory.pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:05
 */
public class VeggiePizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("VeggiePizza.prepare");
    }
}

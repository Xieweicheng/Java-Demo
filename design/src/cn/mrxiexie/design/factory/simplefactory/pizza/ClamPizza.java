package cn.mrxiexie.design.factory.simplefactory.pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:04
 */
public class ClamPizza extends Pizza {

    @Override
    public void prepare() {
        System.out.println("ClamPizza.prepare");
    }
}

package cn.mrxiexie.design.factory.factorymethod.pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:33
 */
public abstract class Pizza {

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
}

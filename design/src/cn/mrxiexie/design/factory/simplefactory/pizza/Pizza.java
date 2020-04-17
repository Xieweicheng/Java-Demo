package cn.mrxiexie.design.factory.simplefactory.pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午9:45
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

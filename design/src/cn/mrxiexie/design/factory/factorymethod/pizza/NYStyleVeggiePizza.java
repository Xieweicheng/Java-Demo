package cn.mrxiexie.design.factory.factorymethod.pizza;

/**
 * @author mrxiexie
 * @date 19-9-15 下午10:34
 */
public class NYStyleVeggiePizza extends Pizza{
    
    @Override
    public void prepare() {
        System.out.println("NYStyleVeggiePizza.prepare");
    }
}

package cn.mrxiexie.design.decorator;

/**
 * @author mrxiexie
 * @date 19-9-15 下午2:45
 */
public class Espresso implements Beverage {

    @Override
    public String getDescription() {
        return "Espresso";
    }

    @Override
    public double cost() {
        return .89;
    }
}

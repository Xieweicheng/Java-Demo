package cn.mrxiexie.design.decorator;

/**
 * @author mrxiexie
 * @date 19-9-15 下午2:49
 */
public class Whip extends CondimentDecorator {

    Beverage beverage;

    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Whip";
    }

    @Override
    public double cost() {
        return .23 + beverage.cost();
    }
}

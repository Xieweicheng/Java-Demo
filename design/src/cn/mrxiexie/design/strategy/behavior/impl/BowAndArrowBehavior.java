package cn.mrxiexie.design.strategy.behavior.impl;

import cn.mrxiexie.design.strategy.behavior.WeaponBehavior;

/**
 * @author mrxiexie
 * @date 9/11/19 3:35 PM
 */
public class BowAndArrowBehavior implements WeaponBehavior {

    @Override
    public void useWeapon() {
        System.out.println("弓箭射击");
    }
}

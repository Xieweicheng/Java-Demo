package cn.mrxiexie.design.strategy.behavior.impl;

import cn.mrxiexie.design.strategy.behavior.WeaponBehavior;

/**
 * @author mrxiexie
 * @date 9/11/19 3:34 PM
 */
public class AxeBehavior implements WeaponBehavior {

    @Override
    public void useWeapon() {
        System.out.println("斧头砍劈");
    }
}

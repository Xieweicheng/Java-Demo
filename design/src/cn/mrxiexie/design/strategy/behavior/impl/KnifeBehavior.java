package cn.mrxiexie.design.strategy.behavior.impl;

import cn.mrxiexie.design.strategy.behavior.WeaponBehavior;

/**
 * @author mrxiexie
 * @date 9/11/19 3:32 PM
 */
public class KnifeBehavior implements WeaponBehavior {

    @Override
    public void useWeapon() {
        System.out.println("匕首刺杀");
    }
}

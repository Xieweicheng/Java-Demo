package cn.mrxiexie.design.strategy.character;

import cn.mrxiexie.design.strategy.behavior.WeaponBehavior;

/**
 * 角色
 *
 * @author mrxiexie
 * @date 9/11/19 3:27 PM
 */
public abstract class AbstractCharacter {

    protected WeaponBehavior weaponBehavior;

    /**
     * 打架
     */
    public abstract void fight();

    public void setWeaponBehavior(WeaponBehavior weaponBehavior) {
        this.weaponBehavior = weaponBehavior;
    }
}

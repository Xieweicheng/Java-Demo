package cn.mrxiexie.design.strategy.character.impl;

import cn.mrxiexie.design.strategy.character.AbstractCharacter;

/**
 * @author mrxiexie
 * @date 9/11/19 3:30 PM
 */
public class Queen extends AbstractCharacter {

    @Override
    public void fight() {
        weaponBehavior.useWeapon();
    }
}

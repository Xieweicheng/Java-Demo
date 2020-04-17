package cn.mrxiexie.design.strategy.character.impl;

import cn.mrxiexie.design.strategy.character.AbstractCharacter;

/**
 * @author mrxiexie
 * @date 9/11/19 3:31 PM
 */
public class Troll extends AbstractCharacter {

    @Override
    public void fight() {
        weaponBehavior.useWeapon();
    }
}

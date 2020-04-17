package cn.mrxiexie.design.strategy;

import cn.mrxiexie.design.strategy.behavior.impl.KnifeBehavior;
import cn.mrxiexie.design.strategy.character.AbstractCharacter;
import cn.mrxiexie.design.strategy.character.impl.King;

/**
 * @author mrxiexie
 * @date 9/11/19 3:36 PM
 */
public class Main {

    public static void main(String[] args) {
        AbstractCharacter character = new King();
        character.setWeaponBehavior(new KnifeBehavior());
        character.fight();
    }
}

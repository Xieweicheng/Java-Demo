package cn.mrxiexie.reflect.parent;

import cn.mrxiexie.reflect.pad.Pad;
import cn.mrxiexie.reflect.phone.Phone;

public interface IParent<K extends Phone, V extends Pad> {

    default V get(K k) {return null;}

    ;
}

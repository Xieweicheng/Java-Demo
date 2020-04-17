package cn.mrxiexie.reflect.parent;

import cn.mrxiexie.reflect.AllAnnotation;
import cn.mrxiexie.reflect.pad.Pad;
import cn.mrxiexie.reflect.phone.Phone;

@AllAnnotation
@ParentAnnotation
public class Parent<K extends Phone, V extends Pad> implements IParent<K, V> {

    private Phone phone;

    private Pad pad;

    @Override
    public V get(K k) {
        return null;
    }

    private String parentClassPrivateField;

    public String parentClassPublicField;

    private String parentClassPrivateMethod() {
        return "";
    }

    public String parentClassPublicMethod() {
        return "";
    }

    public class PublicInnerParent {

    }

    public static class PublicStaticInnerParent {

    }

    private class PrivateInnerParent {

    }

    private static class PrivateStaticInnerParent {

    }
}

package cn.mrxiexie.reflect.son;

import cn.mrxiexie.reflect.pad.IPad;
import cn.mrxiexie.reflect.parent.IParent;
import cn.mrxiexie.reflect.parent.Parent;
import cn.mrxiexie.reflect.phone.IPhone;

@SonAnnotation
@SonAnnotation2
public class Son extends Parent<IPhone, IPad> implements ISon, IParent<IPhone, IPad> {

    @SonFieldAnnotation
    private String sonClassPrivateField;

    @SonFieldAnnotation
    public String sonClassPublicField;

    @SonMethodAnnotation
    private String sonClassPrivateMethod() {
        return "";
    }

    @SonMethodAnnotation
    public String sonClassPublicMethod() {
        return "";
    }

    public class PublicInnerSon {

    }

    public static class PublicStaticInnerSon {

    }

    private class PrivateInnerSon {

    }

    private static class PrivateStaticInnerSon {

    }
}


package cn.mrxiexie.reflect.son;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.mrxiexie.reflect.parent.ParentAnnotation;

@ParentAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface SonAnnotation {
}

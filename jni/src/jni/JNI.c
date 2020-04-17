#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "cn_mrxiexie_jni_JNI.h"

JNIEXPORT void JNICALL Java_cn_mrxiexie_jni_JNI_hello(JNIEnv *env, jclass class) {
   printf("In C Hello World!\n");
   return;
}

JNIEXPORT jstring JNICALL Java_cn_mrxiexie_jni_JNI_getPhone(JNIEnv *env, jclass class) {
    printf("In C getPhone!\n");
    return (*env)->NewStringUTF(env, "110");
}

JNIEXPORT jstring JNICALL Java_cn_mrxiexie_jni_JNI_isHello(JNIEnv *env, jclass class, jstring string) {
    printf("In C isHello\n");
    const char *inCStr = (*env)->GetStringUTFChars(env, string, NULL);
    return strcmp(inCStr, "hello") == 0;
}

JNIEXPORT void JNICALL Java_cn_mrxiexie_jni_JNI_exec (JNIEnv *env, jclass class) {
    printf("In C exec\n");

    jmethodID getTime = (*env)->GetStaticMethodID(env, class, "getTime", "()J");
    if (NULL == getTime) return;
    jobject resultJNIlong = (*env)->CallStaticObjectMethod(env, class, getTime);
    printf("In C exec\n %d", resultJNIlong);
    return;
}
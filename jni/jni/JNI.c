// 导入同目录下的 `cn_mrxiexie_jni_JNI.h` 头文件
#include "cn_mrxiexie_jni_JNI.h"
// 导入系统中同名的头文件，可以理解为 Java 中的 import
#include <jni.h>
// 求平方根需要用到 `math.h` 中的 `double sqrt(double)` 方法
#include <math.h>
// 输入输出
#include <stdio.h>

jmethodID logMethodId;

void java_log(JNIEnv *env, jclass thisClass, char *msg) {
  if (NULL == logMethodId) {
    logMethodId = (*env)->GetStaticMethodID(env, thisClass, "log",
                                            "(Ljava/lang/String;)V");
  }
  jstring message = (*env)->NewStringUTF(env, msg);
  (*env)->CallStaticVoidMethod(env, thisClass, logMethodId, message);
}

/*
 * Class:     cn_mrxiexie_jni_JNI
 * Method:    sqrt
 * Signature: ([I)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_cn_mrxiexie_jni_JNI_sqrt(JNIEnv *env,
                                                             jclass thisClass,
                                                             jintArray nums) {
  java_log(env, thisClass, "sqrt");
  // 将 jintArray 转换为 jint[]
  jint *nums_intPointer = (*env)->GetIntArrayElements(env, nums, NULL);
  if (nums_intPointer == NULL) {
    return 0;
  }
  jsize nums_Len = (*env)->GetArrayLength(env, nums);
  double sqrtArray[nums_Len];
  for (int i = 0; i < nums_Len; i++) {
    sqrtArray[i] = sqrt((double)*(nums_intPointer + i));
  }
  jdoubleArray doublearray_Ret =
      (*env)->NewDoubleArray(env, nums_Len); // 将需要返回的数组长度len，作为入参
  (*env)->SetDoubleArrayRegion(env, doublearray_Ret, 0, nums_Len,
                               sqrtArray); // c_array 对应C代码的数组指针
  return doublearray_Ret;
}

/*
 * Class:     cn_mrxiexie_jni_JNI
 * Method:    max
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_cn_mrxiexie_jni_JNI_max(JNIEnv *env,
                                                    jclass thisClass) {
  java_log(env, thisClass, "max");
  // 获取方法 id
  jmethodID getNums =
      (*env)->GetStaticMethodID(env, thisClass, "getNums", "(I)[I");
  if (NULL == getNums)
    return 0;
  // 调用 Java 中的静态方法
  jintArray numbs = (*env)->CallStaticObjectMethod(env, thisClass, getNums, 10);

  // 将 jintArray 转换为 jint[]
  jint *intArray = (*env)->GetIntArrayElements(env, numbs, NULL);
  jsize length = (*env)->GetArrayLength(env, numbs);
  jint max = *(intArray + 1);
  for (int i = 0; i < length; i++) {
    jint temp = *(intArray + i);
    if (max < temp) {
      max = temp;
    }
  }
  return max;
}

JNIEXPORT jint JNICALL Java_cn_mrxiexie_jni_JNI_min(JNIEnv *env,
                                                    jobject thisObj) {
  // 通过 thisObj 对象获取对象的类
  jclass thisClass = (*env)->GetObjectClass(env, thisObj);
  java_log(env, thisClass, "min");

  // 获取方法 id
  jmethodID getNums =
      (*env)->GetStaticMethodID(env, thisClass, "getNums", "(I)[I");
  if (NULL == getNums)
    return 0;
  // 调用 Java 中的静态方法
  jintArray numbs = (*env)->CallStaticObjectMethod(env, thisClass, getNums, 10);

  // 将 jintArray 转换为 jint[]
  jint *intArray = (*env)->GetIntArrayElements(env, numbs, NULL);
  jsize length = (*env)->GetArrayLength(env, numbs);
  jint min = *(intArray + 1);
  for (int i = 0; i < length; i++) {
    jint temp = *(intArray + i);
    if (min > temp) {
      min = temp;
    }
  }
  return min;
}
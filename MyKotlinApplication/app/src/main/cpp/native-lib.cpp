#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_cpsdna_app_mykotlinapplication_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_cpsdna_app_mykotlinapplication_MainActivity_stringFromJNI2(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello My Baby！";
    return env->NewStringUTF(hello.c_str());
}

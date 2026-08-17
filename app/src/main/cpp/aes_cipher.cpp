#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "AESGuard"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_aesguard_app_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject) {
    std::string hello = "AESGuard C++ is working!";
    return env->NewStringUTF(hello.c_str());
}
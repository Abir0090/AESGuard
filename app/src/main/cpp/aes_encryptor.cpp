// Simple encryption demo
#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "AESGuard"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_aesguard_app_MainActivity_aesEncrypt(
        JNIEnv* env, jobject, jstring jInput, jstring jPassword) {

    const char* input = env->GetStringUTFChars(jInput, nullptr);
    const char* password = env->GetStringUTFChars(jPassword, nullptr);

    if (strlen(password) < 4) {
        env->ReleaseStringUTFChars(jInput, input);
        env->ReleaseStringUTFChars(jPassword, password);
        return env->NewStringUTF("ERROR: Password too short");
    }

    // XOR encryption (simple demo)
    std::string result;
    int passLen = strlen(password);
    for (int i = 0; input[i] != '\0'; i++) {
        result += input[i] ^ password[i % passLen];
    }

    // Convert to hex
    std::string hex;
    char buf[3];
    for (size_t i = 0; i < result.size(); i++) {
        sprintf(buf, "%02x", (unsigned char)result[i]);
        hex += buf;
    }

    env->ReleaseStringUTFChars(jInput, input);
    env->ReleaseStringUTFChars(jPassword, password);

    std::string output = "ENCRYPTED: " + hex;
    return env->NewStringUTF(output.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_aesguard_app_MainActivity_aesDecrypt(
        JNIEnv* env, jobject, jstring jInput, jstring jPassword) {

    const char* input = env->GetStringUTFChars(jInput, nullptr);
    const char* password = env->GetStringUTFChars(jPassword, nullptr);

    if (strlen(password) < 4) {
        env->ReleaseStringUTFChars(jInput, input);
        env->ReleaseStringUTFChars(jPassword, password);
        return env->NewStringUTF("ERROR: Password too short");
    }

    std::string result;
    int passLen = strlen(password);
    for (int i = 0; input[i] != '\0'; i++) {
        result += input[i] ^ password[i % passLen];
    }

    env->ReleaseStringUTFChars(jInput, input);
    env->ReleaseStringUTFChars(jPassword, password);

    std::string output = "DECRYPTED: " + result;
    return env->NewStringUTF(output.c_str());
}
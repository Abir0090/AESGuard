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

    std::string result;
    int passLen = strlen(password);
    for (int i = 0; input[i] != '\0'; i++) {
        result += input[i] ^ password[i % passLen];
    }

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

// NEW: File encryption function
extern "C" JNIEXPORT jstring JNICALL
Java_com_aesguard_app_MainActivity_aesEncryptFile(
        JNIEnv* env, jobject, jstring jInputPath, jstring jOutputPath, jstring jPassword) {

    const char* inputPath = env->GetStringUTFChars(jInputPath, nullptr);
    const char* outputPath = env->GetStringUTFChars(jOutputPath, nullptr);
    const char* password = env->GetStringUTFChars(jPassword, nullptr);

    LOGI("Encrypting file: %s -> %s", inputPath, outputPath);

    if (strlen(password) < 4) {
        env->ReleaseStringUTFChars(jInputPath, inputPath);
        env->ReleaseStringUTFChars(jOutputPath, outputPath);
        env->ReleaseStringUTFChars(jPassword, password);
        return env->NewStringUTF("ERROR: Password too short");
    }

    FILE* inFile = fopen(inputPath, "rb");
    if (!inFile) {
        env->ReleaseStringUTFChars(jInputPath, inputPath);
        env->ReleaseStringUTFChars(jOutputPath, outputPath);
        env->ReleaseStringUTFChars(jPassword, password);
        return env->NewStringUTF("ERROR: Cannot read input file");
    }

    FILE* outFile = fopen(outputPath, "wb");
    if (!outFile) {
        fclose(inFile);
        env->ReleaseStringUTFChars(jInputPath, inputPath);
        env->ReleaseStringUTFChars(jOutputPath, outputPath);
        env->ReleaseStringUTFChars(jPassword, password);
        return env->NewStringUTF("ERROR: Cannot write output file");
    }

    unsigned char buffer[1024];
    unsigned char encrypted[1024];
    int bytesRead;
    int passLen = strlen(password);
    long totalBytes = 0;

    while ((bytesRead = fread(buffer, 1, sizeof(buffer), inFile)) > 0) {
        for (int i = 0; i < bytesRead; i++) {
            encrypted[i] = buffer[i] ^ password[i % passLen];
        }
        fwrite(encrypted, 1, bytesRead, outFile);
        totalBytes += bytesRead;
    }

    fclose(inFile);
    fclose(outFile);

    env->ReleaseStringUTFChars(jInputPath, inputPath);
    env->ReleaseStringUTFChars(jOutputPath, outputPath);
    env->ReleaseStringUTFChars(jPassword, password);

    char result[100];
    sprintf(result, "SUCCESS: Encrypted %ld bytes!", totalBytes);
    return env->NewStringUTF(result);
}
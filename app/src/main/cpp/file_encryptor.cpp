#include <jni.h>
#include <string>
#include <fstream>
#include <android/log.h>

#define LOG_TAG "AESGuard"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_aesguard_app_MainActivity_testFileIO(
        JNIEnv* env,
        jobject,
        jstring jPath) {

    const char* path = env->GetStringUTFChars(jPath, nullptr);
    LOGI("Testing file I/O with path: %s", path);

    // Test: Write to a file
    std::ofstream outFile(path);
    if (outFile.is_open()) {
        outFile << "Hello from AESGuard C++!";
        outFile.close();
        LOGI("File written successfully");
    } else {
        LOGE("Cannot open file for writing");
        env->ReleaseStringUTFChars(jPath, path);
        return env->NewStringUTF("ERROR: Cannot write file");
    }

    // Test: Read from the file
    std::ifstream inFile(path);
    std::string content;
    if (inFile.is_open()) {
        std::string line;
        while (std::getline(inFile, line)) {
            content += line;
        }
        inFile.close();
        LOGI("File read successfully");
    } else {
        LOGE("Cannot open file for reading");
        env->ReleaseStringUTFChars(jPath, path);
        return env->NewStringUTF("ERROR: Cannot read file");
    }

    env->ReleaseStringUTFChars(jPath, path);

    // Return what we read
    std::string result = "File I/O works! Read: " + content;
    return env->NewStringUTF(result.c_str());
}
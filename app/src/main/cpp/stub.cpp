#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_procinex_app_NativeBridge_stringFromJNI(JNIEnv* env, jobject /* this */) {
    std::string hello = "ProCineX native stub";
    return env->NewStringUTF(hello.c_str());
}

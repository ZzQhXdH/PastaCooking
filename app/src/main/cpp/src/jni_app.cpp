//
// Created by admin on 2022/4/21.
//

#include <jni.h>
#include "serial_port.h"
#include "log.h"


extern "C"
JNIEXPORT jlong JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_open(JNIEnv *env, jobject thiz, jstring name) {
    const char *s = env->GetStringUTFChars(name, nullptr);
    auto *port = new SerialPort();
    bool flag = port->open(s);
    env->ReleaseStringUTFChars(name, s);
    if (flag) {
        return reinterpret_cast<jlong>(port);
    }
    delete port;
    return -1;
}


extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_read(JNIEnv *env, jobject thiz, jlong fd) {
    auto *port = reinterpret_cast<SerialPort *>(fd);
    ByteBuf byteBuf;
    std::error_code ec;
    port->read(byteBuf, ec);
    if (ec) {
        return nullptr;
    }
    auto len = static_cast<jsize>(byteBuf.size());
    const auto *data = reinterpret_cast<const jbyte *>(byteBuf.data());
    jbyteArray buf = env->NewByteArray(len);
    env->SetByteArrayRegion(buf, 0, len, data);
    return buf;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_write(JNIEnv *env, jobject thiz, jlong fd,
                                                    jbyteArray buf) {
    jbyte *bytes = env->GetByteArrayElements(buf, nullptr);
    jsize len = env->GetArrayLength(buf);
    auto port = reinterpret_cast<SerialPort *>(fd);
    port->write( reinterpret_cast<const uint8_t *>(bytes), static_cast<uint32_t>(len) );
    env->ReleaseByteArrayElements(buf, bytes, JNI_ABORT);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_close(JNIEnv *env, jobject thiz, jlong fd) {
    auto port = reinterpret_cast<SerialPort *>(fd);
    port->close();
    debug("close port");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_delete(JNIEnv *env, jobject thiz, jlong fd) {
    auto port = reinterpret_cast<SerialPort *>(fd);
    delete port;
    debug("free port");
}


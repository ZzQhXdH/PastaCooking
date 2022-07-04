#include <jni.h>
#include "./conn/serial_port.h"
//
// Created by admin on 2022/7/4.
//

using namespace conn;

extern "C"
JNIEXPORT jlong JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_00024Companion_open(JNIEnv *env,
                          jobject thiz,
                          jstring name,
                          jint baud_rate) {

    const char *c_name = env->GetStringUTFChars(name, nullptr);
    int fd;
    bool ret = serial_port_t::open(c_name, baud_rate, fd);
    env->ReleaseStringUTFChars(name, c_name);
    if (!ret) {
        return -1;
    }
    auto port = new serial_port_t(fd);
    return reinterpret_cast<jlong>(port);
}



extern "C"
JNIEXPORT void JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_close(JNIEnv *env, jobject thiz, jlong fd) {
    auto port = reinterpret_cast<serial_port_t *>(fd);
    delete port;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_read(JNIEnv *env, jobject thiz, jlong fd,
                                               jbyteArray buf) {

    auto port = reinterpret_cast<serial_port_t *>(fd);
    jbyte *c_buf = env->GetByteArrayElements(buf, nullptr);
    jint c_size = env->GetArrayLength(buf);

    int size = port->read(c_buf, c_size);

    env->ReleaseByteArrayElements(buf, c_buf, 0);

    return size;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_write(JNIEnv *env, jobject thiz, jlong fd,
                                                jbyteArray buf) {

    auto port = reinterpret_cast<serial_port_t *>(fd);
    jbyte *c_buf = env->GetByteArrayElements(buf, nullptr);
    jint c_size = env->GetArrayLength(buf);

    int size = port->write(c_buf, c_size);

    env->ReleaseByteArrayElements(buf, c_buf, JNI_ABORT);

    return size;
}



extern "C"
JNIEXPORT jint JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_readByte(JNIEnv *env, jobject thiz, jlong fd) {
    auto port = reinterpret_cast<serial_port_t *>(fd);
    uint8_t v;

    for (;;) {
        int size = port->read(&v, 1);
        if (size < 0) {
            return size;
        }
        if (size != 0) {
            break;
        }
    }

    return v;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_readUInt16(JNIEnv *env, jobject thiz, jlong fd) {
    auto port = reinterpret_cast<serial_port_t *>(fd);
    uint8_t buf[2];
    int index = 0;

    for (; index < 2;) {
        int size = port->read(buf + index, 2 - index);
        if (size < 0) {
            return size;
        }
        index += size;
    }

    return (buf[0] << 8) + buf[1];
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_hontech_pastacooking_conn_SerialPort_readBuf(JNIEnv *env, jobject thiz, jlong fd, jbyteArray buf) {

    auto port = reinterpret_cast<serial_port_t *>(fd);
    jbyte *c_buf = env->GetByteArrayElements(buf, nullptr);
    jint c_size = env->GetArrayLength(buf);
    int index = 0;
    int size = 0;

    for (; index < c_size; ) {
        size = port->read(c_buf + index, c_size - index);
        if (size < 0) {
            goto finish;
        }
        index += size;
    }
    size = index;

finish:
    env->ReleaseByteArrayElements(buf, c_buf, 0);

    return size;
}














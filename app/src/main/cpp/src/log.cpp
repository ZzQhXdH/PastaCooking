//
// Created by admin on 2022/5/4.
//
#include <android/log.h>
#include <stdarg.h>
#include <stdio.h>
#include "log.h"

void log(int prio, const char *tag, const char *format, ...) {
    va_list ap;
    va_start(ap, format);
    __android_log_vprint(prio, tag, format, ap);
}

void debug(const char *format, ...) {
    va_list ap;
    va_start(ap, format);
    __android_log_vprint(ANDROID_LOG_DEBUG, "debug", format, ap);
}

void error(const char *format, ...) {
    va_list ap;
    va_start(ap, format);
    __android_log_vprint(ANDROID_LOG_ERROR, "error", format, ap);
}


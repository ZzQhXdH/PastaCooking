//
// Created by admin on 2022/4/22.
//

#ifndef PASTACOOKING_DECODE_H
#define PASTACOOKING_DECODE_H

#include <stdint.h>

inline uint8_t decode_uint8(const uint8_t *buf) {
    return buf[0];
}

inline uint16_t decode_uint16(const uint8_t *buf) {
    return (buf[0] << 8) + buf[1];
}

inline uint32_t decode_uint32(const uint8_t *buf) {
    return (buf[0] << 24) +
            (buf[1] << 16) +
            (buf[2] << 8) +
            (buf[3]);
}

#endif //PASTACOOKING_DECODE_H

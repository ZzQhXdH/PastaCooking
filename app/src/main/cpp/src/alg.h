//
// Created by admin on 2022/4/22.
//

#ifndef PASTACOOKING_ALG_H
#define PASTACOOKING_ALG_H

#include <stdint.h>

template <typename T>
inline T xor_sum(const T* buf, uint32_t size) {
    T sum = 0;
    for (uint32_t i = 0; i < size; i ++) {
        sum ^= buf[i];
    }
    return sum;
}

#endif //PASTACOOKING_ALG_H

//
// Created by admin on 2022/4/21.
//

#ifndef PASTACOOKING_SERIAL_PORT_H
#define PASTACOOKING_SERIAL_PORT_H

#include <string>
#include <memory>
#include "byte_buf.h"
#include <system_error>

class SerialPort {

public:
    struct Impl;

    SerialPort();
    ~SerialPort();

    bool open(const std::string &name);

    void write(const uint8_t *buf, uint32_t size);

    void read(ByteBuf &buf, std::error_code &ec);

private:
    std::unique_ptr<Impl> impl;
};


#endif //PASTACOOKING_SERIAL_PORT_H

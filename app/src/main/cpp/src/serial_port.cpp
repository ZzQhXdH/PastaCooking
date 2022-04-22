//
// Created by admin on 2022/4/21.
//

#include "serial_port.h"
#include "asio.hpp"
#include "alg.h"
#include "decode.h"
#include "error.h"

constexpr uint8_t Head0 = 0xE1;
constexpr uint8_t Head1 = 0x1E;
constexpr uint8_t End = 0xEF;

struct SerialPort::Impl {
    asio::io_context ctx;
    asio::serial_port port;

    Impl() : ctx(1), port(ctx) {}

    bool open(const std::string &name) {
        using S = asio::serial_port;
        std::error_code ec;
        port.open(name, ec);
        if (ec) {
            return false;
        }
        port.set_option(S::baud_rate(115200));
        port.set_option(S::character_size(8));
        port.set_option(S::stop_bits(S::stop_bits::one));
        port.set_option(S::parity(S::parity::none));
        port.set_option(S::flow_control(S::flow_control::none));
        return true;
    }

    bool write(const uint8_t *buf, uint32_t size) {
        std::error_code ec;
        asio::write(port, asio::buffer(buf, size), ec);
        return !ec;
    }

    void sync(std::error_code &ec) {
        bool flag = false;
        uint8_t v;
        for (;;) {
            asio::read(port, asio::buffer(&v, 1), ec);
            if (ec) {
                return;
            }
            if (flag && (v == Head1)) {
                return;
            }
            flag = (v == Head0);
        }
    }

    uint16_t readLen(std::error_code &ec) {
        uint8_t buf[2];
        asio::read(port, asio::buffer(buf, 2), ec);
        if (ec) {
            return 0;
        }
        return decode_uint16(buf);
    }

    void read(ByteBuf &buf, std::error_code &ec) {
        sync(ec);
        if (ec) {
            return;
        }

        uint16_t len = readLen(ec);
        if (ec) {
            return;
        }

        if (len < 10) {
            ec = make_ec(EC_LEN_FAIL);
            return;
        }

        len -= 4;
        buf.resize(len);
        asio::read(port, asio::buffer(buf), ec);
        if (ec) {
            return;
        }

        // dest(0), src(1), req(2), data(4)
        const uint8_t *data = buf.data();
        uint8_t sum = xor_sum(data + 4, len - 6);
        if (sum != data[len - 2]) {
            ec = make_ec(EC_SUM_FAIL);
            return;
        }

        if (End != data[len - 1]) {
            ec = make_ec(EC_END_FAIL);
            return;
        }
    }
};

SerialPort::SerialPort() :
    impl(std::make_unique<Impl>())
{

}

SerialPort::~SerialPort() {

}

bool SerialPort::open(const std::string &name) {
    return impl->open(name);
}

void SerialPort::write(const uint8_t *buf, uint32_t size) {
    impl->write(buf, size);
}

void SerialPort::read(ByteBuf &buf, std::error_code &ec) {
    impl->read(buf, ec);
}








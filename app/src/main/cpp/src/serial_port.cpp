//
// Created by admin on 2022/4/21.
//

#include "serial_port.h"
#include "asio.hpp"
#include "alg.h"
#include "decode.h"
#include "error.h"
#include "log.h"

constexpr uint8_t Head0 = 0xE1;
constexpr uint8_t Head1 = 0x1E;
constexpr uint8_t End = 0xEF;

struct FrameReader {

public:
    FrameReader(asio::serial_port &p, ByteBuf &b, std::error_code &ece) : port(p), buf(b), ec(ece) {}

    void read() {
        read_head();
    }

private:
    void read_head() {
        port.async_read_some(asio::buffer(&tmp, 1),
                             [this](const std::error_code &e, size_t) {
            if (e) {
                this->ec = e;
                return;
            }
            if (this->flag && (this->tmp == Head1)) {
                this->read_len();
                return;
            }
            this->flag = (this->tmp == Head0);
            this->read_head();
        });
    }

    void read_len() {
        asio::async_read(port, asio::buffer(len_buf, 2),
                   [this](const std::error_code &e, size_t) {
            if (e) {
                this->ec = e;
                return;
            }
            uint16_t len = decode_uint16(len_buf);
            if (len < 10) {
                this->ec = make_ec(EC_LEN_FAIL);
                return;
            }
            this->read_body(len);
        });
    }

    void read_body(uint16_t len) {
        len = len - 4;
        buf.resize(len);
        asio::async_read(port, asio::buffer(buf),
                   [this] (const std::error_code &e, size_t) {
            if (e) {
                this->ec = e;
                return;
            }
            const uint8_t *data = buf.data();
            uint16_t len = buf.size();
            // dest(0), src(1), req(2), data(4)
            uint8_t sum = xor_sum(data + 4, len - 6);
            if (sum != data[len - 2]) {
                this->ec = make_ec(EC_SUM_FAIL);
                return;
            }

            if (End != data[len - 1]) {
                this->ec = make_ec(EC_END_FAIL);
                return;
            }
        });
    }

private:
    asio::serial_port &port;
    ByteBuf &buf;
    std::error_code &ec;
    uint8_t tmp{};
    bool flag = false;
    uint8_t len_buf[2]{};
};

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

    [[nodiscard]] bool is_open() const {
        return port.is_open();
    }

    void close() {
        std::error_code ec;
        port.close(ec);
    }


    bool write(const uint8_t *buf, uint32_t size) {
        std::error_code ec;
        asio::write(port, asio::buffer(buf, size), ec);
        return !ec;
    }

    void read(ByteBuf &buf, std::error_code &ec) {
        FrameReader reader(port, buf, ec);
        reader.read();
        ctx.restart();
        ctx.run();
    }
};

SerialPort::SerialPort() :
    impl(std::make_unique<Impl>())
{

}

SerialPort::~SerialPort() {
    if (impl->is_open()) {
        impl->close();
    }
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

void SerialPort::close() {
    impl->close();
}








//
// Created by admin on 2022/7/4.
//

#include "serial_port.h"
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>
#include <jni.h>
#include <android/log.h>


namespace conn {

bool serial_port_t::open(const char *name, int baudrate, int &fd) {

    fd = ::open(name, O_RDWR | O_NOCTTY | O_NDELAY);
    if (fd < 0) {
        return false;
    }

    return set_attr(fd, baudrate);
}

bool serial_port_t::set_attr(int fd, int baudrate) {

    struct termios tty{};

    fcntl(fd, F_SETFL, 0);
    if (tcgetattr(fd, &tty) != 0) {
        return false;
    }

    cfmakeraw(&tty);

    tty.c_cflag &= ~PARENB;
    tty.c_iflag &= ~(INPCK);

    tty.c_cflag &= ~CSTOPB;

    tty.c_cflag &= ~CSIZE;
    tty.c_cflag |= CS8;

    tty.c_cflag &= ~CRTSCTS;

    tty.c_lflag &= ~(ICANON | ECHO | ECHOE | ECHOK | ECHONL | NOFLSH | ISIG);

    tty.c_oflag &= ~(OPOST);

    tty.c_cc[VTIME] = 5;
    tty.c_cc[VMIN] = 0;

    int baud = B115200;
    if (baudrate == 9600) {
        baud = B9600;
    }
    cfsetispeed(&tty, baud);
    cfsetospeed(&tty, baud);

    return tcsetattr(fd, TCSANOW, &tty) == 0;
}

serial_port_t::serial_port_t(int fd) : m_fd(fd) {
}

serial_port_t::~serial_port_t() {
    ::close(m_fd);
}

int serial_port_t::read(void *buf, int size) const {
    return ::read(m_fd, buf, static_cast<size_t>(size));
}

int serial_port_t::write(void *buf, int size) const {
    return ::write(m_fd, buf, static_cast<size_t>(size));
}


}

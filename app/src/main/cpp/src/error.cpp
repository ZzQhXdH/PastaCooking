//
// Created by admin on 2022/4/22.
//

#include "error.h"

const char *SerialErrorCategory::name() const noexcept {
    return "SerialErrorCategory";
}

std::string SerialErrorCategory::message(int ec) const {
    return std::string("SerialErrorCategory:") + std::to_string(ec);
}

static SerialErrorCategory serialCategory;

std::error_code make_ec(int ec) {
    return std::error_code(ec, serialCategory);
}


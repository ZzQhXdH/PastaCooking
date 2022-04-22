//
// Created by admin on 2022/4/22.
//

#ifndef PASTACOOKING_ERROR_H
#define PASTACOOKING_ERROR_H

#include <system_error>


class SerialErrorCategory : public std::error_category {

public:
    const char *name() const _NOEXCEPT override;

    std::string message(int ec) const override;
};

std::error_code make_ec(int ec);

constexpr int EC_LEN_FAIL = 1;
constexpr int EC_SUM_FAIL = 2;
constexpr int EC_END_FAIL = 3;

#endif //PASTACOOKING_ERROR_H

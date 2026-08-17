cmake_minimum_required(VERSION 3.22.1)
project("aesguard")

add_library(aesguard SHARED
        aes_cipher.cpp
)

find_library(log-lib log)
target_link_libraries(aesguard ${log-lib})
if(TARGET mbgl-vendor-metal-cpp)
    return()
endif()

add_library(
    mbgl-vendor-metal-cpp INTERFACE
)

target_include_directories(
    mbgl-vendor-metal-cpp SYSTEM
    INTERFACE ${CMAKE_CURRENT_LIST_DIR}/metal-cpp
)

set_target_properties(
    mbgl-vendor-metal-cpp
    PROPERTIES
        INTERFACE_MAPVINA_NAME "metal-cpp"
        INTERFACE_MAPVINA_URL "https://developer.apple.com/metal/cpp/"
        INTERFACE_MAPVINA_AUTHOR "Apple"
        INTERFACE_MAPVINA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/metal-cpp/LICENSE.txt
)

add_library(mapvina-native-base INTERFACE)
add_library(MapVinaNative::Base ALIAS mapvina-native-base)

target_include_directories(mapvina-native-base SYSTEM INTERFACE
    "${PROJECT_SOURCE_DIR}/include"
)

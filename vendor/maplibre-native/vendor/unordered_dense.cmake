if (CMAKE_VERSION VERSION_GREATER_EQUAL "3.25")
    add_subdirectory(${PROJECT_SOURCE_DIR}/vendor/unordered_dense SYSTEM)
else()
    add_subdirectory(${PROJECT_SOURCE_DIR}/vendor/unordered_dense)
endif()

set_target_properties(
    unordered_dense
    PROPERTIES
        INTERFACE_MAPVINA_NAME "unordered_dense"
        INTERFACE_MAPVINA_URL "https://github.com/martinus/unordered_dense"
        INTERFACE_MAPVINA_AUTHOR "Martin Leitner-Ankerl"
        INTERFACE_MAPVINA_LICENSE ${PROJECT_SOURCE_DIR}/vendor/unordered_dense/LICENSE
)

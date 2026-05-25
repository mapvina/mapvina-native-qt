// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#include "qgeoserviceproviderplugin.hpp"

#include "../qt_mapping_engine.hpp"

QGeoServiceProviderFactoryMapVina::QGeoServiceProviderFactoryMapVina() = default;

QGeoCodingManagerEngine *QGeoServiceProviderFactoryMapVina::createGeocodingManagerEngine(
    const QVariantMap &parameters, QGeoServiceProvider::Error *error, QString *errorString) const {
    Q_UNUSED(parameters);
    Q_UNUSED(error);
    Q_UNUSED(errorString);

    return nullptr;
}

QGeoMappingManagerEngine *QGeoServiceProviderFactoryMapVina::createMappingManagerEngine(
    const QVariantMap &parameters, QGeoServiceProvider::Error *error, QString *errorString) const {
    // NOLINTNEXTLINE(cppcoreguidelines-owning-memory)
    return new QMapVina::QtMappingEngine(parameters, error, errorString);
}

QGeoRoutingManagerEngine *QGeoServiceProviderFactoryMapVina::createRoutingManagerEngine(
    const QVariantMap &parameters, QGeoServiceProvider::Error *error, QString *errorString) const {
    Q_UNUSED(parameters);
    Q_UNUSED(error);
    Q_UNUSED(errorString);

    return nullptr;
}

QPlaceManagerEngine *QGeoServiceProviderFactoryMapVina::createPlaceManagerEngine(const QVariantMap &parameters,
                                                                                  QGeoServiceProvider::Error *error,
                                                                                  QString *errorString) const {
    Q_UNUSED(parameters);
    Q_UNUSED(error);
    Q_UNUSED(errorString);

    return nullptr;
}

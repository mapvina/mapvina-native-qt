// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#pragma once

#include <QtCore/QObject>
#include <QtLocation/QGeoServiceProviderFactory>

class QGeoServiceProviderFactoryMapVina : public QObject, public QGeoServiceProviderFactory {
    Q_OBJECT
    Q_INTERFACES(QGeoServiceProviderFactory)
#if QT_VERSION >= QT_VERSION_CHECK(6, 5, 0)
    Q_PLUGIN_METADATA(IID "org.qt-project.qt.geoservice.serviceproviderfactory/6.0" FILE "mapvina_plugin.json")
#else
    Q_PLUGIN_METADATA(IID "org.qt-project.qt.geoservice.serviceproviderfactory/5.0" FILE "mapvina_plugin.json")
#endif

public:
    QGeoServiceProviderFactoryMapVina();

    QGeoCodingManagerEngine *createGeocodingManagerEngine(const QVariantMap &parameters,
                                                          QGeoServiceProvider::Error *error,
                                                          QString *errorString) const override;
    QGeoMappingManagerEngine *createMappingManagerEngine(const QVariantMap &parameters,
                                                         QGeoServiceProvider::Error *error,
                                                         QString *errorString) const override;
    QGeoRoutingManagerEngine *createRoutingManagerEngine(const QVariantMap &parameters,
                                                         QGeoServiceProvider::Error *error,
                                                         QString *errorString) const override;
    QPlaceManagerEngine *createPlaceManagerEngine(const QVariantMap &parameters,
                                                  QGeoServiceProvider::Error *error,
                                                  QString *errorString) const override;
};

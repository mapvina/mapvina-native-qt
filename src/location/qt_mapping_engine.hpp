// Copyright (C) 2023 MapVina contributors
// Copyright (C) 2017 The Qt Company Ltd.
// Copyright (C) 2017 Mapbox, Inc.

// SPDX-License-Identifier: LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only

#pragma once

#include "export_location.hpp"

#include <QMapVina/Settings>

#include <QtLocation/private/qgeomappingmanagerengine_p.h>
#include <QtLocation/QGeoServiceProvider>

namespace QMapVina {

class Q_MAPVINA_LOCATION_EXPORT QtMappingEngine : public QGeoMappingManagerEngine {
    Q_OBJECT

public:
    QtMappingEngine(const QVariantMap &parameters, QGeoServiceProvider::Error *error, QString *errorString);

    QGeoMap *createMap() override;

private:
    Settings m_settings;
    QString m_mapItemsBefore;
};

} // namespace QMapVina

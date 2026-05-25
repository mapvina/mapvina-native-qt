// Copyright (C) 2023 MapVina contributors
// Copyright (C) 2017 The Qt Company Ltd.
// Copyright (C) 2017 Mapbox, Inc.

// SPDX-License-Identifier: LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only

#pragma once

#include "export_location.hpp"

#include <QMapVina/Map>
#include <QMapVina/StyleParameter>

#include <QtLocation/private/qgeomap_p.h>

namespace QMapVina {

class QGeoMapMapVinaPrivate;

class Q_MAPVINA_LOCATION_EXPORT QGeoMapMapVina : public QGeoMap {
    Q_OBJECT
    Q_DECLARE_PRIVATE(QGeoMapMapVina)

public:
    explicit QGeoMapMapVina(QGeoMappingManagerEngine *engine, QObject *parent = nullptr);
    ~QGeoMapMapVina() override;

    [[nodiscard]] Capabilities capabilities() const override;

    void setSettings(const Settings &settings);
    void setMapItemsBefore(const QString &mapItemsBefore);

    void addStyleParameter(StyleParameter *parameter);
    void removeStyleParameter(StyleParameter *parameter);
    void clearStyleParameters();

private Q_SLOTS:
    // QMapVina
    void onMapChanged(Map::MapChange);

    // QDeclarativeGeoMapItemBase
    void onMapItemPropertyChanged();
    void onMapItemSubPropertyChanged();
    void onMapItemUnsupportedPropertyChanged();
    void onMapItemGeometryChanged();

    // StyleParameter
    void onStyleParameterUpdated(StyleParameter *parameter);

private:
    QSGNode *updateSceneGraph(QSGNode *oldNode, QQuickWindow *window) override;
};

} // namespace QMapVina

// Copyright (C) 2023 MapVina contributors
// Copyright (C) 2017 The Qt Company Ltd.
// Copyright (C) 2017 Mapbox, Inc.

// SPDX-License-Identifier: LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only

#include "qt_mapping_engine.hpp"
#include "qgeomap.hpp"
#include "types.hpp"

#include <QtLocation/private/qabstractgeotilecache_p.h>
#include <QtLocation/private/qgeocameracapabilities_p.h>
#include <QtLocation/private/qgeomaptype_p.h>

#include <QtCore/QDir>
#include <QtCore/QJsonArray>
#include <QtCore/QJsonDocument>
#include <QtCore/QJsonObject>
#include <QtCore/QJsonValue>
#include <QtCore/QStandardPaths>
#include <QtCore/QVariant>
#include <QtQml/QJSValue>

#include <memory>

namespace {

void parseStyleJsonObject(QMapVina::Styles &styles, const QJsonObject &obj) {
    if (!obj.contains(QStringLiteral("url"))) {
        return;
    }

    const QString url = obj.value(QStringLiteral("url")).toString();
    if (url.isEmpty()) {
        return;
    }

    const QString name = obj.contains(QStringLiteral("name"))
                             ? obj.value(QStringLiteral("name")).toString()
                             : QStringLiteral("Style") + QString::number(styles.size() + 1);
    QMapVina::Style style(url, name);
    if (obj.contains(QStringLiteral("description"))) {
        style.description = obj.value(QStringLiteral("description")).toString();
    }
    if (obj.contains(QStringLiteral("night"))) {
        style.night = obj.value(QStringLiteral("night")).toBool();
    }
    if (obj.contains("type")) {
        style.type = QMapVina::Style::Type(obj.value(QStringLiteral("type")).toInt());
    }
    if (obj.contains("style")) {
        style.type = QMapVina::Style::Type(obj.value(QStringLiteral("style")).toInt());
    }
    styles.append(std::move(style));
}

} // namespace

namespace QMapVina {

QtMappingEngine::QtMappingEngine(const QVariantMap &parameters,
                                 QGeoServiceProvider::Error *error,
                                 QString *errorString) {
    *error = QGeoServiceProvider::NoError;
    errorString->clear();

    // constants
    constexpr int tileSize{512};
    constexpr double maxZoom{20.0};
    constexpr double maxTilt{60.0};
    constexpr double fieldOfView{36.87};

    // capabilities
    QGeoCameraCapabilities cameraCaps;
    cameraCaps.setMinimumZoomLevel(0.0);
    cameraCaps.setMaximumZoomLevel(maxZoom);
    cameraCaps.setTileSize(tileSize);
    cameraCaps.setSupportsBearing(true);
    cameraCaps.setSupportsTilting(true);
    cameraCaps.setMinimumTilt(0);
    cameraCaps.setMaximumTilt(maxTilt);
    cameraCaps.setMinimumFieldOfView(fieldOfView);
    cameraCaps.setMaximumFieldOfView(fieldOfView);
    setCameraCapabilities(cameraCaps);

    const QStringList supportedOptions{QStringLiteral("mapvina.api.provider"),
                                       QStringLiteral("mapvina.api.base_url"),
                                       QStringLiteral("mapvina.api.key"),
                                       QStringLiteral("mapvina.map.styles"),
                                       QStringLiteral("mapvina.cache.memory"),
                                       QStringLiteral("mapvina.cache.directory"),
                                       QStringLiteral("mapvina.cache.size"),
                                       QStringLiteral("mapvina.items.insert_before"),
                                       QStringLiteral("mapvina.client.name"),
                                       QStringLiteral("mapvina.client.version")};
    for (const QString &key : parameters.keys()) {
        if (!supportedOptions.contains(key)) {
            qWarning() << "Unsupported option" << key;
        }
    }

    // API settings
    if (parameters.contains(QStringLiteral("mapvina.api.provider"))) {
        const QString apiProvider = parameters.value(QStringLiteral("mapvina.api.provider")).toString();
        if (apiProvider == "mapvina") {
            m_settings.setProviderTemplate(Settings::MapVinaProvider);
        } else if (apiProvider == "maptiler") {
            m_settings.setProviderTemplate(Settings::MapTilerProvider);
        } else if (apiProvider == "mapbox") {
            m_settings.setProviderTemplate(Settings::MapboxProvider);
        } else {
            qWarning() << "Unknown API provider" << apiProvider;
        }
    }

    if (parameters.contains(QStringLiteral("mapvina.api.base_url"))) {
        m_settings.setApiBaseUrl(parameters.value(QStringLiteral("mapvina.api.base_url")).toString());
    }

    if (parameters.contains(QStringLiteral("mapvina.api.key"))) {
        m_settings.setApiKey(parameters.value(QStringLiteral("mapvina.api.key")).toString());
    }

    // map styles
    const QByteArray pluginName = "mapvina";
    QList<QGeoMapType> mapTypes;
    int mapId{};

    if (parameters.contains(QStringLiteral("mapvina.map.styles"))) {
        Styles styles;
        const QVariant stylesValue = parameters.value(QStringLiteral("mapvina.map.styles"));
        if (stylesValue.userType() == qMetaTypeId<QJSValue>()) {
            auto jsonDoc = QJsonDocument::fromVariant(stylesValue.value<QJSValue>().toVariant());
            if (jsonDoc.isObject()) {
                parseStyleJsonObject(styles, jsonDoc.object());
            } else if (jsonDoc.isArray()) {
                for (const auto &value : jsonDoc.array()) {
                    if (value.isObject()) {
                        parseStyleJsonObject(styles, value.toObject());
                    } else if (value.isString()) {
                        const QString url = value.toString();
                        if (url.isEmpty()) {
                            continue;
                        }
                        styles.append(Style(url, "Style " + QString::number(styles.size() + 1)));
                    }
                }
            }
        } else {
            const QString urls = parameters.value(QStringLiteral("mapvina.map.styles")).toString();
            const QStringList urlsList = urls.split(',', Qt::SkipEmptyParts);

            for (const QString &url : urlsList) {
                if (url.isEmpty()) {
                    continue;
                }
                styles.append(Style(url, "Style " + QString::number(styles.size() + 1)));
            }
        }

        m_settings.setStyles(styles);

        for (const Style &style : styles) {
            QVariantMap mapMetadata;
            mapMetadata[QStringLiteral("url")] = style.url;
            mapMetadata[QStringLiteral("isHTTPS")] = style.url.startsWith(QStringLiteral("https:"));

            mapTypes << QGeoMapType(static_cast<QGeoMapType::MapStyle>(style.type),
                                    style.name,
                                    style.description,
                                    false,
                                    style.night,
                                    ++mapId,
                                    pluginName,
                                    cameraCaps,
                                    mapMetadata);
        }
    }

    // load provider styles if available
    for (const Style &style : m_settings.providerStyles()) {
        QVariantMap mapMetadata;
        mapMetadata[QStringLiteral("url")] = style.url;
        mapMetadata[QStringLiteral("isHTTPS")] = true;

        mapTypes << QGeoMapType(static_cast<QGeoMapType::MapStyle>(style.type),
                                style.name,
                                style.description,
                                false,
                                style.night,
                                ++mapId,
                                pluginName,
                                cameraCaps,
                                mapMetadata);
    }

    setSupportedMapTypes(mapTypes);

    // cache
    bool memoryCache{};
    if (parameters.contains(QStringLiteral("mapvina.cache.memory"))) {
        memoryCache = parameters.value(QStringLiteral("mapvina.cache.memory")).toBool();
        m_settings.setCacheDatabasePath(QStringLiteral(":memory:"));
    }

    QString cacheDirectory;
    if (parameters.contains(QStringLiteral("mapvina.cache.directory"))) {
        cacheDirectory = parameters.value(QStringLiteral("mapvina.cache.directory")).toString();
    } else {
        cacheDirectory = QAbstractGeoTileCache::baseLocationCacheDirectory() + QStringLiteral("mapvina");
    }

    if (!memoryCache && QDir::root().mkpath(cacheDirectory)) {
        m_settings.setCacheDatabasePath(cacheDirectory + "/mapvina.db");
    }

    if (parameters.contains(QStringLiteral("mapvina.cache.size"))) {
        bool ok{};
        const QString cacheSizeString = parameters.value(QStringLiteral("mapvina.cache.size")).toString();
        const int cacheSize = cacheSizeString.toInt(&ok);

        if (ok) {
            m_settings.setCacheDatabaseMaximumSize(cacheSize);
        } else {
            qWarning() << "Invalid cache size" << cacheSizeString;
        }
    }

    // rendering
    if (parameters.contains(QStringLiteral("mapvina.items.insert_before"))) {
        m_mapItemsBefore = parameters.value(QStringLiteral("mapvina.items.insert_before")).toString();
    }

    // client
    if (parameters.contains(QStringLiteral("mapvina.client.name"))) {
        m_settings.setClientName(parameters.value(QStringLiteral("mapvina.client.name")).toString());
    }

    if (parameters.contains(QStringLiteral("mapvina.client.version"))) {
        m_settings.setClientVersion(parameters.value(QStringLiteral("mapvina.client.version")).toString());
    }

    engineInitialized();
}

QGeoMap *QtMappingEngine::createMap() {
    auto map = std::make_unique<QGeoMapMapVina>(this);
    map->setSettings(m_settings);
    map->setMapItemsBefore(m_mapItemsBefore);
    return map.release();
}

} // namespace QMapVina

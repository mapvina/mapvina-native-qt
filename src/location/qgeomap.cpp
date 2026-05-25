// Copyright (C) 2023 MapVina contributors
// Copyright (C) 2017 The Qt Company Ltd.
// Copyright (C) 2017 Mapbox, Inc.

// SPDX-License-Identifier: LGPL-3.0-only OR GPL-2.0-only OR GPL-3.0-only

#include "qgeomap.hpp"
#include "layer_style_change_p.hpp"
#include "qgeomap_p.hpp"
#include "source_style_change_p.hpp"
#include "style_change_p.hpp"
#include "style_change_utils_p.hpp"
#include "texture_node_base_p.hpp"
#ifdef MLN_RENDER_BACKEND_OPENGL
#include "texture_node_opengl_p.hpp"
#endif
#ifdef MLN_RENDER_BACKEND_METAL
#include "texture_node_metal_p.hpp"
#endif
#ifdef MLN_RENDER_BACKEND_VULKAN
#include "texture_node_vulkan_p.hpp"
#endif

#include <QMapVina/Types>

#include <QtLocation/private/qdeclarativecirclemapitem_p.h>
#include <QtLocation/private/qdeclarativegeomapitembase_p.h>
#include <QtLocation/private/qdeclarativepolygonmapitem_p.h>
#include <QtLocation/private/qdeclarativepolylinemapitem_p.h>
#include <QtLocation/private/qdeclarativerectanglemapitem_p.h>
#include <QtLocation/private/qgeoprojection_p.h>

#include <QtCore/QByteArray>
#include <QtCore/QCoreApplication>
#include <QtQuick/QQuickWindow>
#include <QtQuick/QSGImageNode>
#include <QtQuick/QSGRendererInterface>
#ifdef MLN_RENDER_BACKEND_OPENGL
#include <QtQuick/private/qsgcontext_p.h> // for debugging the context name
#include <QtGui/QOpenGLContext>
#endif

#include <algorithm>
#include <cmath>
#include <numbers>

namespace {

constexpr int mapVinaTileSize{512};
constexpr double invLog2 = 1.0 / std::numbers::ln2;

double zoomLevelFrom256(double zoomLevelFor256, double tileSize) {
    constexpr double size256{256.0};
    constexpr double zoomScaleBase{2.0};
    return std::log(std::pow(zoomScaleBase, zoomLevelFor256) * size256 / tileSize) * invLog2;
}

} // namespace

namespace QMapVina {

QGeoMapMapVinaPrivate::QGeoMapMapVinaPrivate(QGeoMappingManagerEngine *engine)
    : QGeoMapPrivate(engine, new QGeoProjectionWebMercator) {}

QGeoMapMapVinaPrivate::~QGeoMapMapVinaPrivate() = default;

QSGNode *QGeoMapMapVinaPrivate::updateSceneGraph(QSGNode *node, QQuickWindow *window) {
    const Q_Q(QGeoMapMapVina);

    if (m_viewportSize.isEmpty()) {
        delete node; // NOLINT(cppcoreguidelines-owning-memory)
        return nullptr;
    }

    Map *map{};
    if (node == nullptr) {
#ifdef MLN_RENDER_BACKEND_OPENGL
        // OpenGL context check
        const QOpenGLContext *glContext = QOpenGLContext::currentContext();
        if (glContext == nullptr) {
            qWarning("QOpenGLContext is NULL!");
            qWarning() << "You are running on QSG backend " << QSGContext::backend();
            qWarning("The MapVina plugin works with both Desktop and ES 2.0+ OpenGL versions.");
            qWarning("Verify that your Qt is built with OpenGL, and what kind of OpenGL.");
            qWarning(
                "To force using a specific OpenGL version, check QSurfaceFormat::setRenderableType and "
                "QSurfaceFormat::setDefaultFormat");

            return node;
        }

        std::unique_ptr<TextureNodeBase> mbglNode = std::make_unique<TextureNodeOpenGL>(
            m_settings, m_viewportSize, window->devicePixelRatio());
#elif defined(MLN_RENDER_BACKEND_METAL)
        std::unique_ptr<TextureNodeBase> mbglNode = std::make_unique<TextureNodeMetal>(
            m_settings, m_viewportSize, window->devicePixelRatio());
#elif defined(MLN_RENDER_BACKEND_VULKAN)
        std::unique_ptr<TextureNodeBase> mbglNode = std::make_unique<TextureNodeVulkan>(
            m_settings, m_viewportSize, window->devicePixelRatio());
#endif
        QObject::connect(mbglNode->map(), &Map::needsRendering, q, &QGeoMap::sgNodeChanged);
        mbglNode->map()->setConnectionEstablished();

        QObject::connect(mbglNode->map(), &Map::mapChanged, q, &QGeoMapMapVina::onMapChanged);
        m_syncState = MapTypeSync | CameraDataSync | ViewportSync | VisibleAreaSync;
        node = mbglNode.release();
    }
    map = static_cast<TextureNodeBase *>(node)->map();

    if ((m_syncState & MapTypeSync) != 0 && m_activeMapType.metadata().contains(QStringLiteral("url"))) {
        map->setStyleUrl(m_activeMapType.metadata()[QStringLiteral("url")].toString());
    }

    if ((m_syncState & VisibleAreaSync) != 0) {
        if (m_visibleArea.isEmpty()) {
            map->setMargins(QMargins());
        } else {
            const QMargins margins(
                static_cast<int>(m_visibleArea.x()),                                                     // left
                static_cast<int>(m_visibleArea.y()),                                                     // top
                static_cast<int>(m_viewportSize.width() - m_visibleArea.width() - m_visibleArea.x()),    // right
                static_cast<int>(m_viewportSize.height() - m_visibleArea.height() - m_visibleArea.y())); // bottom
            map->setMargins(margins);
        }
    }

    if ((m_syncState & CameraDataSync) != 0 || (m_syncState & VisibleAreaSync) != 0) {
        map->setZoom(zoomLevelFrom256(m_cameraData.zoomLevel(), mapVinaTileSize));
        map->setBearing(m_cameraData.bearing());
        map->setPitch(m_cameraData.tilt());

        const QGeoCoordinate coordinate = m_cameraData.center();
        map->setCoordinate(Coordinate(coordinate.latitude(), coordinate.longitude()));
    }

    if ((m_syncState & ViewportSync) != 0) {
        static_cast<TextureNodeBase *>(node)->resize(m_viewportSize, window->devicePixelRatio(), window);
    }

    if (m_styleLoaded) {
        syncStyleChanges(map);
    }

    static_cast<TextureNodeBase *>(node)->render(window);

#ifdef MLN_RENDER_BACKEND_OPENGL
    // Threaded rendering hack only needd for OpenGL
    threadedRenderingHack(window, map);
#endif

    m_syncState = NoSync;

    return node;
}

QGeoMap::ItemTypes QGeoMapMapVinaPrivate::supportedMapItemTypes() const {
    return QGeoMap::MapRectangle | QGeoMap::MapCircle | QGeoMap::MapPolygon | QGeoMap::MapPolyline;
}

void QGeoMapMapVinaPrivate::addMapItem(QDeclarativeGeoMapItemBase *item) {
    Q_Q(QGeoMapMapVina);

    switch (item->itemType()) {
        case QGeoMap::NoItem:
        case QGeoMap::MapQuickItem:
        case QGeoMap::CustomMapItem:
            return;
        case QGeoMap::MapRectangle: {
            auto *mapItem = static_cast<QDeclarativeRectangleMapItem *>(item);
            QObject::connect(mapItem, &QQuickItem::visibleChanged, q, &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(mapItem,
                             &QDeclarativeGeoMapItemBase::mapItemOpacityChanged,
                             q,
                             &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(mapItem,
                             &QDeclarativeRectangleMapItem::bottomRightChanged,
                             q,
                             &QGeoMapMapVina::onMapItemGeometryChanged);
            QObject::connect(
                mapItem, &QDeclarativeRectangleMapItem::topLeftChanged, q, &QGeoMapMapVina::onMapItemGeometryChanged);
            QObject::connect(
                mapItem, &QDeclarativeRectangleMapItem::colorChanged, q, &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(mapItem->border(),
                             &QDeclarativeMapLineProperties::colorChanged,
                             q,
                             &QGeoMapMapVina::onMapItemSubPropertyChanged);
            QObject::connect(mapItem->border(),
                             &QDeclarativeMapLineProperties::widthChanged,
                             q,
                             &QGeoMapMapVina::onMapItemUnsupportedPropertyChanged);
        } break;
        case QGeoMap::MapCircle: {
            auto *mapItem = static_cast<QDeclarativeCircleMapItem *>(item);
            QObject::connect(mapItem, &QQuickItem::visibleChanged, q, &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(mapItem,
                             &QDeclarativeGeoMapItemBase::mapItemOpacityChanged,
                             q,
                             &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(
                mapItem, &QDeclarativeCircleMapItem::centerChanged, q, &QGeoMapMapVina::onMapItemGeometryChanged);
            QObject::connect(
                mapItem, &QDeclarativeCircleMapItem::radiusChanged, q, &QGeoMapMapVina::onMapItemGeometryChanged);
            QObject::connect(
                mapItem, &QDeclarativeCircleMapItem::colorChanged, q, &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(mapItem->border(),
                             &QDeclarativeMapLineProperties::colorChanged,
                             q,
                             &QGeoMapMapVina::onMapItemSubPropertyChanged);
            QObject::connect(mapItem->border(),
                             &QDeclarativeMapLineProperties::widthChanged,
                             q,
                             &QGeoMapMapVina::onMapItemUnsupportedPropertyChanged);
        } break;
        case QGeoMap::MapPolygon: {
            auto *mapItem = static_cast<QDeclarativePolygonMapItem *>(item);
            QObject::connect(mapItem, &QQuickItem::visibleChanged, q, &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(mapItem,
                             &QDeclarativeGeoMapItemBase::mapItemOpacityChanged,
                             q,
                             &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(
                mapItem, &QDeclarativePolygonMapItem::pathChanged, q, &QGeoMapMapVina::onMapItemGeometryChanged);
            QObject::connect(
                mapItem, &QDeclarativePolygonMapItem::colorChanged, q, &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(mapItem->border(),
                             &QDeclarativeMapLineProperties::colorChanged,
                             q,
                             &QGeoMapMapVina::onMapItemSubPropertyChanged);
            QObject::connect(mapItem->border(),
                             &QDeclarativeMapLineProperties::widthChanged,
                             q,
                             &QGeoMapMapVina::onMapItemUnsupportedPropertyChanged);
        } break;
        case QGeoMap::MapPolyline: {
            auto *mapItem = static_cast<QDeclarativePolylineMapItem *>(item);
            QObject::connect(mapItem, &QQuickItem::visibleChanged, q, &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(mapItem,
                             &QDeclarativeGeoMapItemBase::mapItemOpacityChanged,
                             q,
                             &QGeoMapMapVina::onMapItemPropertyChanged);
            QObject::connect(
                mapItem, &QDeclarativePolylineMapItem::pathChanged, q, &QGeoMapMapVina::onMapItemGeometryChanged);
            QObject::connect(mapItem->line(),
                             &QDeclarativeMapLineProperties::colorChanged,
                             q,
                             &QGeoMapMapVina::onMapItemSubPropertyChanged);
            QObject::connect(mapItem->line(),
                             &QDeclarativeMapLineProperties::widthChanged,
                             q,
                             &QGeoMapMapVina::onMapItemSubPropertyChanged);
        } break;
    }

    QObject::connect(
        item, &QDeclarativeGeoMapItemBase::mapItemOpacityChanged, q, &QGeoMapMapVina::onMapItemPropertyChanged);

    std::vector<std::unique_ptr<StyleChange>> changes = StyleChange::addFeature(
        StyleChangeUtils::featureFromMapItem(item),
        StyleChangeUtils::featurePropertiesFromMapItem(item),
        m_mapItemsBefore);
    std::ranges::move(changes, std::back_inserter(m_styleChanges));

    emit q->sgNodeChanged();
}

void QGeoMapMapVinaPrivate::removeMapItem(QDeclarativeGeoMapItemBase *item) {
    Q_Q(QGeoMapMapVina);

    switch (item->itemType()) {
        case QGeoMap::NoItem:
        case QGeoMap::MapQuickItem:
        case QGeoMap::CustomMapItem:
            return;
        case QGeoMap::MapRectangle:
            q->disconnect(static_cast<QDeclarativeRectangleMapItem *>(item)->border());
            break;
        case QGeoMap::MapCircle:
            q->disconnect(static_cast<QDeclarativeCircleMapItem *>(item)->border());
            break;
        case QGeoMap::MapPolygon:
            q->disconnect(static_cast<QDeclarativePolygonMapItem *>(item)->border());
            break;
        case QGeoMap::MapPolyline:
            q->disconnect(static_cast<QDeclarativePolylineMapItem *>(item)->line());
            break;
    }

    q->disconnect(item);

    std::vector<std::unique_ptr<StyleChange>> changes = StyleChange::removeFeature(
        StyleChangeUtils::featureFromMapItem(item));
    std::ranges::move(changes, std::back_inserter(m_styleChanges));

    emit q->sgNodeChanged();
}

void QGeoMapMapVinaPrivate::addStyleParameter(StyleParameter *parameter) {
    Q_Q(QGeoMapMapVina);

    if (m_mapParameters.contains(parameter)) {
        return;
    }

    m_mapParameters << parameter;

    QObject::connect(parameter, &StyleParameter::updated, q, &QGeoMapMapVina::onStyleParameterUpdated);

    if (m_styleLoaded) {
        std::vector<std::unique_ptr<StyleChange>> changes = StyleChange::addParameter(parameter, m_mapItemsBefore);
        std::ranges::move(changes, std::back_inserter(m_styleChanges));
        emit q->sgNodeChanged();
    }
}

void QGeoMapMapVinaPrivate::removeStyleParameter(StyleParameter *parameter) {
    Q_Q(QGeoMapMapVina);

    q->disconnect(parameter);

    if (m_styleLoaded) {
        std::vector<std::unique_ptr<StyleChange>> changes = StyleChange::removeParameter(parameter);
        std::ranges::move(changes, std::back_inserter(m_styleChanges));
        emit q->sgNodeChanged();
    }

    m_mapParameters.removeOne(parameter);
}

void QGeoMapMapVinaPrivate::clearStyleParameters() {
    for (StyleParameter *parameter : m_mapParameters) {
        removeStyleParameter(parameter);
    }
}

void QGeoMapMapVinaPrivate::changeViewportSize(const QSize & /* size */) {
    Q_Q(QGeoMapMapVina);

    m_syncState = m_syncState | ViewportSync;
    emit q->sgNodeChanged();
}

void QGeoMapMapVinaPrivate::changeCameraData(const QGeoCameraData & /* data */) {
    Q_Q(QGeoMapMapVina);

    m_syncState = m_syncState | CameraDataSync;
    emit q->sgNodeChanged();
}

#if QT_VERSION >= QT_VERSION_CHECK(6, 5, 0)
void QGeoMapMapVinaPrivate::changeActiveMapType(const QGeoMapType & /* mapType */)
#else
void QGeoMapMapVinaPrivate::changeActiveMapType(const QGeoMapType /* mapType */)
#endif
{
    Q_Q(QGeoMapMapVina);

    m_syncState = m_syncState | MapTypeSync;
    emit q->sgNodeChanged();
}

void QGeoMapMapVinaPrivate::setVisibleArea(const QRectF &visibleArea) {
    Q_Q(QGeoMapMapVina);
    const QRectF va = clampVisibleArea(visibleArea);
    if (va == m_visibleArea) {
        return;
    }

    m_visibleArea = va;
    m_geoProjection->setVisibleArea(va);

    m_syncState = m_syncState | VisibleAreaSync;
    emit q->sgNodeChanged();
}

QRectF QGeoMapMapVinaPrivate::visibleArea() const {
    return m_visibleArea;
}

void QGeoMapMapVinaPrivate::syncStyleChanges(Map *map) {
    for (const auto &change : m_styleChanges) {
        if (change->isValid()) {
            change->apply(map);
        }
    }

    m_styleChanges.clear();
}

void QGeoMapMapVinaPrivate::threadedRenderingHack(QQuickWindow *window, Map *map) {
    // Detect threaded rendering. Some backends require a hack where we need
    // to set a timer to update the map until all the resources are loaded,
    // which is not exactly battery friendly, because might trigger more paints
    // than we need.
#ifdef MLN_RENDER_BACKEND_OPENGL
    if (!m_threadedRenderingChecked) {
        m_threadedRendering = static_cast<QOpenGLContext *>(window->rendererInterface()->getResource(
                                                                window, QSGRendererInterface::OpenGLContextResource))
                                  ->thread() != QCoreApplication::instance()->thread();

        m_threadedRenderingChecked = true;
    }
#else
    Q_UNUSED(window);
#endif

    // Fallback timer to keep updating until map fully loaded when threaded rendering is active.
    if (m_threadedRendering) {
        if (!map->isFullyLoaded()) {
            QMetaObject::invokeMethod(&m_refresh, "start", Qt::QueuedConnection);
        } else {
            QMetaObject::invokeMethod(&m_refresh, "stop", Qt::QueuedConnection);
        }
    }
}

/*
 * QGeoMapMapVina implementation
 */

QGeoMapMapVina::QGeoMapMapVina(QGeoMappingManagerEngine *engine, QObject *parent)
    : QGeoMap(*new QGeoMapMapVinaPrivate(engine), parent) {
    Q_D(QGeoMapMapVina);

    constexpr int refreshInterval{250};

    connect(&d->m_refresh, &QTimer::timeout, this, &QGeoMap::sgNodeChanged);
    d->m_refresh.setInterval(refreshInterval);
}

QGeoMapMapVina::~QGeoMapMapVina() = default;

void QGeoMapMapVina::setSettings(const Settings &settings) {
    Q_D(QGeoMapMapVina);

    d->m_settings = settings;
}

void QGeoMapMapVina::setMapItemsBefore(const QString &before) {
    Q_D(QGeoMapMapVina);
    d->m_mapItemsBefore = before;
}

void QGeoMapMapVina::addStyleParameter(StyleParameter *parameter) {
    Q_D(QGeoMapMapVina);
    d->addStyleParameter(parameter);
}

void QGeoMapMapVina::removeStyleParameter(StyleParameter *parameter) {
    Q_D(QGeoMapMapVina);
    d->removeStyleParameter(parameter);
}

void QGeoMapMapVina::clearStyleParameters() {
    Q_D(QGeoMapMapVina);
    d->clearStyleParameters();
}

QGeoMap::Capabilities QGeoMapMapVina::capabilities() const {
    return {SupportsVisibleRegion | SupportsSetBearing | SupportsAnchoringCoordinate | SupportsVisibleArea};
}

QSGNode *QGeoMapMapVina::updateSceneGraph(QSGNode *oldNode, QQuickWindow *window) {
    Q_D(QGeoMapMapVina);
    return d->updateSceneGraph(oldNode, window);
}

void QGeoMapMapVina::onMapChanged(Map::MapChange change) {
    Q_D(QGeoMapMapVina);

    if (change == Map::MapChangeDidFinishLoadingStyle || change == Map::MapChangeDidFailLoadingMap) {
        d->m_styleLoaded = true;
    } else if (change == Map::MapChangeWillStartLoadingMap) {
        d->m_styleLoaded = false;
        d->m_styleChanges.clear();

        for (QDeclarativeGeoMapItemBase *item : d->m_mapItems) {
            std::vector<std::unique_ptr<StyleChange>> changes = StyleChange::addFeature(
                StyleChangeUtils::featureFromMapItem(item),
                StyleChangeUtils::featurePropertiesFromMapItem(item),
                d->m_mapItemsBefore);
            std::ranges::move(changes, std::back_inserter(d->m_styleChanges));
        }

        for (const StyleParameter *parameter : d->m_mapParameters) {
            std::vector<std::unique_ptr<StyleChange>> changes = StyleChange::addParameter(parameter,
                                                                                          d->m_mapItemsBefore);
            std::ranges::move(changes, std::back_inserter(d->m_styleChanges));
        }
    } else if (change == Map::MapChangeDidFinishLoadingMap) {
        constexpr int refreshInterval{250};
        // TODO: make it more elegant
        QTimer::singleShot(refreshInterval, this, &QGeoMapMapVina::sgNodeChanged);
    }
}

void QGeoMapMapVina::onMapItemPropertyChanged() {
    Q_D(QGeoMapMapVina);

    auto *item = static_cast<QDeclarativeGeoMapItemBase *>(sender());
    const QString id = StyleChangeUtils::featureId(item);
    for (const FeatureProperty &property : StyleChangeUtils::featurePropertiesFromMapItem(item)) {
        if (property.type == FeatureProperty::LayoutProperty) {
            d->m_styleChanges.emplace_back(
                std::make_unique<StyleSetLayoutProperties>(id, property.name, property.value));
        } else if (property.type == FeatureProperty::PaintProperty) {
            d->m_styleChanges.emplace_back(
                std::make_unique<StyleSetPaintProperties>(id, property.name, property.value));
        }
    }

    emit sgNodeChanged();
}

void QGeoMapMapVina::onMapItemSubPropertyChanged() {
    Q_D(QGeoMapMapVina);

    auto *item = static_cast<QDeclarativeGeoMapItemBase *>(sender()->parent());
    const QString id = StyleChangeUtils::featureId(item);
    for (const FeatureProperty &property : StyleChangeUtils::featurePropertiesFromMapItem(item)) {
        // only paint properties should be handled
        if (property.type == FeatureProperty::PaintProperty) {
            d->m_styleChanges.emplace_back(
                std::make_unique<StyleSetPaintProperties>(id, property.name, property.value));
        }
    }

    emit sgNodeChanged();
}

void QGeoMapMapVina::onMapItemUnsupportedPropertyChanged() {
    // TODO https://bugreports.qt.io/browse/QTBUG-58872
    qWarning() << "Unsupported property for managed Map item";
}

void QGeoMapMapVina::onMapItemGeometryChanged() {
    Q_D(QGeoMapMapVina);

    auto *item = static_cast<QDeclarativeGeoMapItemBase *>(sender());
    d->m_styleChanges.push_back(std::make_unique<StyleAddSource>(StyleChangeUtils::featureFromMapItem(item)));

    emit sgNodeChanged();
}

void QGeoMapMapVina::onStyleParameterUpdated(StyleParameter *parameter) {
    Q_D(QGeoMapMapVina);

    std::vector<std::unique_ptr<StyleChange>> changes = StyleChange::addParameter(parameter, d->m_mapItemsBefore);
    std::ranges::move(changes, std::back_inserter(d->m_styleChanges));

    emit sgNodeChanged();
}

} // namespace QMapVina

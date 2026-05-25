// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINAQMLTYPES_H
#define QMAPVINAQMLTYPES_H

#include "declarative_style.hpp"

#include <QtLocation/private/qdeclarativegeomap_p.h>

#include <QtQml/QQmlEngine>

class MapVinaStyleAttached : public QObject {
    Q_OBJECT
    QML_ANONYMOUS
    Q_PROPERTY(QMapVina::DeclarativeStyle *style READ style WRITE setStyle NOTIFY styleChanged)
public:
    explicit MapVinaStyleAttached(QObject *parent)
        : QObject(parent) {}

    [[nodiscard]] QMapVina::DeclarativeStyle *style() const { return m_style; }

    void setStyle(QMapVina::DeclarativeStyle *style) {
        m_style = style;
        Q_EMIT styleChanged(m_style);

        // Check for QQuickItem
        auto *quickItem = qobject_cast<QQuickItem *>(parent());
        if (quickItem == nullptr) {
            qWarning() << "Not a QQuickItem!";
            return;
        }

        // Check for MapView
        QDeclarativeGeoMap *declarativeMap{};
        if (QString(quickItem->metaObject()->className()).startsWith("MapView")) {
            declarativeMap = QQmlProperty::read(quickItem, "map").value<QDeclarativeGeoMap *>();
        } else {
            declarativeMap = qobject_cast<QDeclarativeGeoMap *>(parent());
        }

        if (declarativeMap == nullptr) {
            qWarning() << "Map object not found!";
            return;
        }

        style->setDeclarativeMap(declarativeMap);
    }

Q_SIGNALS:
    void styleChanged(QMapVina::DeclarativeStyle *style);

private:
    QMapVina::DeclarativeStyle *m_style{};
};

class MapVinaStyleProperties : public QObject {
    Q_OBJECT
    QML_ATTACHED(MapVinaStyleAttached)
    QML_NAMED_ELEMENT(MapVina)

public:
    // NOLINTNEXTLINE(cppcoreguidelines-owning-memory)
    static MapVinaStyleAttached *qmlAttachedProperties(QObject *object) { return new MapVinaStyleAttached(object); }
};

#endif // QMAPVINAQMLTYPES_H

// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#pragma once

#include "../qgeomap.hpp"
#include "style_parameter.hpp"

#include <QMapVina/StyleParameter>

#include <QtQml/QQmlEngine>
#include <QtQuick/QQuickItem>

QT_BEGIN_NAMESPACE

class QDeclarativeGeoMap;

QT_END_NAMESPACE

namespace QMapVina {

class DeclarativeStyle : public QQuickItem {
    Q_OBJECT
    QML_NAMED_ELEMENT(Style)
#if QT_VERSION >= QT_VERSION_CHECK(6, 0, 0)
    QML_ADDED_IN_VERSION(3, 0)
#endif

public:
    explicit DeclarativeStyle(QQuickItem *parent = nullptr);
    ~DeclarativeStyle() override = default;

    void setDeclarativeMap(QDeclarativeGeoMap *map);
    void setMap(QGeoMapMapVina *map);

    Q_INVOKABLE void addParameter(StyleParameter *parameter);
    Q_INVOKABLE void removeParameter(StyleParameter *parameter);
    Q_INVOKABLE void clearParameters();
    QList<QObject *> parameters();

protected:
    void componentComplete() override;

private:
    void populateParameters();

    QGeoMapMapVina *m_map{};

    QList<StyleParameter *> m_parameters;
};

} // namespace QMapVina

QML_DECLARE_TYPE(QMapVina::DeclarativeStyle)

// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_LAYER_PARAMETER_H
#define QMAPVINA_LAYER_PARAMETER_H

#include "style_parameter.hpp"

#include <QMapVina/Export>

#include <QtCore/QJsonObject>
#include <QtCore/QObject>
#include <QtCore/QString>

namespace QMapVina {

class Q_MAPVINA_CORE_EXPORT LayerParameter : public StyleParameter {
    Q_OBJECT
public:
    explicit LayerParameter(QObject *parent = nullptr);
    ~LayerParameter() override;

    [[nodiscard]] QString type() const;
    void setType(const QString &type);

    [[nodiscard]] QJsonObject layout() const;
    void setLayout(const QJsonObject &layout);
    Q_INVOKABLE void setLayoutProperty(const QString &key, const QVariant &value);

    [[nodiscard]] QJsonObject paint() const;
    void setPaint(const QJsonObject &paint);
    Q_INVOKABLE void setPaintProperty(const QString &key, const QVariant &value);

Q_SIGNALS:
    void layoutUpdated();
    void paintUpdated();

protected:
    QString m_type;
    QJsonObject m_layout;
    QJsonObject m_paint;

    Q_DISABLE_COPY(LayerParameter)
};

} // namespace QMapVina

#endif // QMAPVINA_LAYER_PARAMETER_H

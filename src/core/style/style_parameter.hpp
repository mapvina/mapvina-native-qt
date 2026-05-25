// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_STYLE_PARAMETER_H
#define QMAPVINA_STYLE_PARAMETER_H

#include <QMapVina/Export>

#include <QtCore/QObject>
#include <QtCore/QString>
#include <QtCore/QVariantMap>

namespace QMapVina {

class Q_MAPVINA_CORE_EXPORT StyleParameter : public QObject {
    Q_OBJECT
public:
    explicit StyleParameter(QObject *parent = nullptr);
    ~StyleParameter() override;

    bool operator==(const StyleParameter &other) const;

    [[nodiscard]] inline bool isReady() const { return m_ready; };

    [[nodiscard]] virtual QVariant parsedProperty(const char *propertyName) const;
    bool hasProperty(const char *propertyName) const;
    void updateProperty(const char *propertyName, const QVariant &value);

    [[nodiscard]] QVariantMap toVariantMap() const;

    [[nodiscard]] QString styleId() const;
    void setStyleId(const QString &id);

public Q_SLOTS:
    void updateNotify();

Q_SIGNALS:
    void ready(StyleParameter *parameter);
    void updated(StyleParameter *parameter);

protected:
    const int m_initialPropertyCount = staticMetaObject.propertyCount();
    bool m_ready{};

    QString m_styleId;

    Q_DISABLE_COPY(StyleParameter)
};

} // namespace QMapVina

#endif // QMAPVINA_STYLE_PARAMETER_H

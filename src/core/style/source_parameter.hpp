// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_SOURCE_PARAMETER_H
#define QMAPVINA_SOURCE_PARAMETER_H

#include "style_parameter.hpp"

#include <QMapVina/Export>

#include <QtCore/QObject>
#include <QtCore/QString>

namespace QMapVina {

class Q_MAPVINA_CORE_EXPORT SourceParameter : public StyleParameter {
    Q_OBJECT
public:
    explicit SourceParameter(QObject *parent = nullptr);
    ~SourceParameter() override;

    [[nodiscard]] QString type() const;
    void setType(const QString &type);

protected:
    QString m_type;

    Q_DISABLE_COPY(SourceParameter)
};

} // namespace QMapVina

#endif // QMAPVINA_SOURCE_PARAMETER_H

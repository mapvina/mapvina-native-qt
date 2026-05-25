// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_FILTER_PARAMETER_H
#define QMAPVINA_FILTER_PARAMETER_H

#include "style_parameter.hpp"

#include <QMapVina/Export>

#include <QtCore/QObject>
#include <QtCore/QString>
#include <QtCore/QVariantList>

namespace QMapVina {

class Q_MAPVINA_CORE_EXPORT FilterParameter : public StyleParameter {
    Q_OBJECT
public:
    explicit FilterParameter(QObject *parent = nullptr);
    ~FilterParameter() override;

    [[nodiscard]] QVariantList expression() const;
    void setExpression(const QVariantList &expression);

Q_SIGNALS:
    void expressionUpdated();

protected:
    QVariantList m_expression;

    Q_DISABLE_COPY(FilterParameter)
};

} // namespace QMapVina

#endif // QMAPVINA_FILTER_PARAMETER_H

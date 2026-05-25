// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#include "filter_parameter.hpp"

#include "style_parameter.hpp"

namespace QMapVina {

/*!
    \class FilterParameter
    \brief A helper utility to manage filter parameters for a layer.
    \ingroup QMapVina

    \headerfile filter_parameter.hpp <QMapVina/FilterParameter>
*/

/*!
    \brief Default constructor
*/
FilterParameter::FilterParameter(QObject *parent)
    : StyleParameter(parent) {}

FilterParameter::~FilterParameter() = default;

/*!
    \fn void FilterParameter::expressionUpdated()
    \brief Signal emitted when the filter expression is updated.
*/

/*!
    \brief Filter expression.
    \return \c QVariantList.
*/
QVariantList FilterParameter::expression() const {
    return m_expression;
}

/*!
    \brief Set the filter expression.
    \param expression Filter expression as \c QVariantList.

    \ref expressionUpdated() signal is emitted when the expression is updated.
*/
void FilterParameter::setExpression(const QVariantList &expression) {
    if (m_expression == expression) {
        return;
    }

    m_expression = expression;

    Q_EMIT expressionUpdated();
}

/*!
    \var FilterParameter::m_expression
    \brief Filter expression
*/

} // namespace QMapVina

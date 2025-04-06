package com.example.openbanking.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class JdbcTemplateSelector(
    private val readOnlyNamedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) {

    fun getJdbcTemplate() = if (DatasourceTypeAspect.isCurrentlyReadonly()) {
        readOnlyNamedParameterJdbcTemplate
    } else {
        namedParameterJdbcTemplate
    }
}
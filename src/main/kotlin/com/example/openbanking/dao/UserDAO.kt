package com.example.openbanking.dao

import com.example.openbanking.utlis.uuid
import org.springframework.dao.support.DataAccessUtils
import java.util.UUID
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class UserDAO(
    private val jdbcTemplateSelector: JdbcTemplateSelector,
) {

    private val userRowMapper = RowMapper { rs, _ ->
        User(
            id = rs.getLong("id"),
            name = rs.getString("name"),
            phone = rs.getString("phone"),
            passwordHash = rs.getString("password_hash").uuid(),
        )
    }

    fun findByPhone(phone: String): User? {
        val sql = """
            SELECT id, name, phone, password_hash
            FROM users
            WHERE phone = :phone8 OR phone = :phone7
        """.trimIndent()

        val params = mapOf(
            "phone8" to "8${phone.substring(1)}",
            "phone7" to "7${phone.substring(1)}",
        )

        return DataAccessUtils.singleResult(
            jdbcTemplateSelector.getJdbcTemplate().query(sql, params, userRowMapper)
        )
    }
}

data class User(
    val id: Long,
    val name: String,
    val phone: String,
    val passwordHash: UUID,
)
package com.notificator.bot.cucumber.helper

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.test.jdbc.JdbcTestUtils

@Component
class DbHelper(
    private val jdbcTemplate: JdbcTemplate
) {

    fun clearTables(vararg tableNames: String) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, *tableNames)
    }

    fun insert(sql: String) = jdbcTemplate.update(sql)

    fun update(sql: String) = jdbcTemplate.update(sql)

    fun queryForMap(sql: String): MutableMap<String, Any> = jdbcTemplate.queryForMap(sql)
}

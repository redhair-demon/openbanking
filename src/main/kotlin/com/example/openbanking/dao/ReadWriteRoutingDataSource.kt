package com.example.openbanking.dao

import javax.sql.DataSource
import org.slf4j.LoggerFactory
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

/**
 * Роутер определения ключа, при выполнении запросов.
 *
 * Поведение по умолчанию:
 * - запрос выполняется через дефолтный jdbcTemplate/namedJdbcTemplate без транзакции - *write* реплика
 * - запрос выполняется в транзакции через дефолтный jdbcTemplate/namedJdbcTemplate - *write* реплика
 * - запрос выполняется в транзакции через дефолтный jdbcTemplate/namedJdbcTemplate с флагом readOnly=true - *read* реплика
 * - запрос выполняется без транзакции через readOnlyNamedJdbcTemplate - *read* реплика
 * - запрос выполняется внутри транзакции через readOnlyNamedJdbcTemplate - *read* реплика
 */
class ReadWriteRoutingDataSource(
    writeDatasource: DataSource,
    readDatasource: DataSource,
) : AbstractRoutingDataSource() {

    /**
     * Определенный read datasource для установки в кастомный jdbcTemplate для выполнения запросов на реплику
     */
    val readDataSource: DataSource

    companion object {
        private val logger = LoggerFactory.getLogger(ReadWriteRoutingDataSource::class.java)

        private val currentDataSource = ThreadLocal<DatasourceType>()

        /**
         * Возвращает признак того, какой тип тип datasource выбран для потока
         */
        fun isCurrentlyReadonly(): Boolean {
            return currentDataSource.get() == DatasourceType.READ
        }

        /**
         * Установка типа datasource
         */
        fun setDatasourceType(isReadOnly: Boolean) {
            val datasourceType = if (isReadOnly) DatasourceType.READ else DatasourceType.WRITE
            logger.trace("set datasourceType to {}", datasourceType)
            currentDataSource.set(datasourceType)
        }

        /**
         * Очистка выбранного datasource
         */
        fun cleanCurrentDataSourceType() {
            currentDataSource.set(null)
        }
    }

    init {
        super.setTargetDataSources(
            mapOf(
                DatasourceType.WRITE to writeDatasource,
                DatasourceType.READ to readDatasource,
            ),
        )
        super.setDefaultTargetDataSource(writeDatasource)

        readDataSource = readDatasource
    }

    /**
     * Определение текущего ключа поиска datasouce
     *
     * Если не был задан тип datasource (запрос выполняется без транзакции), тогда оставляем [DatasourceType.WRITE], для
     * сохранения поведения по-умолчанию.
     */
    override fun determineCurrentLookupKey(): Any {
        return currentDataSource.get() ?: return DatasourceType.WRITE
    }

    override fun determineTargetDataSource(): DataSource {
        return super.determineTargetDataSource()
            .also { Companion.logger.trace("resolve dataSource to {}", it) }
    }

    enum class DatasourceType {
        WRITE, READ
    }
}
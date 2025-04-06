package com.example.openbanking.dao

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DatasourceTypeSetter(
    val type: ReadWriteRoutingDataSource.DatasourceType,
)
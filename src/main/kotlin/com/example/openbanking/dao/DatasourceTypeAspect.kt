package com.example.openbanking.dao

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class DatasourceTypeAspect {
    companion object {
        val currentDataSourceType = ThreadLocal<ReadWriteRoutingDataSource.DatasourceType>()

        fun isCurrentlyReadonly(): Boolean {
            return currentDataSourceType.get() == ReadWriteRoutingDataSource.DatasourceType.READ
        }
    }

    @Around("@annotation(datasourceTypeSetter)")
    fun setDatasourceType(joinPoint: ProceedingJoinPoint, datasourceTypeSetter: DatasourceTypeSetter): Any? {
        try {
            currentDataSourceType.set(datasourceTypeSetter.type)
            return joinPoint.proceed()
        } finally {
            currentDataSourceType.set(null)
        }
    }
}
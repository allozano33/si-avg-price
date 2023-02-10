package com.mercadolibre.si_avg_price.model.datadog

enum class MetricName(
    val id: String
) {

    ERROR_PARSING(id = "error_parsing"),
    UNPROVISION_WITHOUT_PROVISION(id = "unprovision_without_provision"),
    PROVISION_WAS_NOT_EXECUTED(id = "provision_was_not_executed"),
    UNPROVISION_WAS_NOT_CREATED_WITH_PROVISION(id = "unprovision_was_not_created_with_provision"),
    UNPROVISION_NUMBER_WERE_PROCESSED(id = "unprovision_number_were_processed"),
    PROVISION_WAS_EXECUTED(id = "provision_was_executed"),
    UNPROVISION_WAS_EXECUTED(id = "unprovision_was_executed")

}
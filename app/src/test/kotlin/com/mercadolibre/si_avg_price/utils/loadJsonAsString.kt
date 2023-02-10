package com.mercadolibre.si_avg_price.utils

import java.io.File

fun loadJsonAsString(path: String) =
    File(path).readText(Charsets.UTF_8)

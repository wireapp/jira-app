package io.github.yamilmedina.app

import io.github.yamilmedina.app.api.registerRoutes
import io.github.yamilmedina.app.infra.config.AppConfig

private const val APP_PORT = 8888
fun main() {
    AppConfig
        .setup()
        .apply { registerRoutes() }
        .start(APP_PORT)
}
package com.wire.apps.jira

import com.wire.apps.jira.api.registerRoutes
import com.wire.apps.jira.infra.config.AppConfig

private const val APP_PORT = 8888

fun main() {
    AppConfig
        .setup()
        .apply { registerRoutes() }
        .start(APP_PORT)
}

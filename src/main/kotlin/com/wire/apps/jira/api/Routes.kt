package com.wire.apps.jira.api

import io.javalin.Javalin
import org.koin.java.KoinJavaComponent.inject

fun Javalin.registerRoutes() {
    get("/") { context -> context.redirect("/health") }
    val healthController: HealthController by inject(HealthController::class.java)
    get("/health", healthController::get)
}

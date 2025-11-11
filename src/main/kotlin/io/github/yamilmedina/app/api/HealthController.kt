package io.github.yamilmedina.app.api

import io.javalin.http.Context

class HealthController() {

    fun get(context: Context) {
        context.json(mapOf("status" to "UP"))
    }

}
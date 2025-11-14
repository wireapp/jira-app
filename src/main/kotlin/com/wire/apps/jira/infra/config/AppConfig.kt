package com.wire.apps.jira.infra.config

import com.wire.apps.jira.di.ModulesConfig.allModules
import com.wire.sdk.service.WireApplicationManager
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

object AppConfig : KoinComponent {
    fun setup(): Javalin {
        startKoin { modules(allModules) }

        // Eagerly initialize WireApplicationManager to start listening to events
        // How can this be improved? for now respects the architecture boundaries.
        val wireManager: WireApplicationManager by inject()
        wireManager // trigger initialization

        val app =
            Javalin.create().apply {
                exception(Exception::class.java) { exception, _ ->
                    exception.printStackTrace()
                    error(HttpStatus.INTERNAL_SERVER_ERROR) { context ->
                        context.json(
                            HttpStatus.INTERNAL_SERVER_ERROR.message,
                        )
                    }
                }
            }
        return app
    }
}

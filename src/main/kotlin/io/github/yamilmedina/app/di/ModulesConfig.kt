package io.github.yamilmedina.app.di

import com.wire.sdk.WireAppSdk
import com.wire.sdk.WireEventsHandlerDefault
import com.wire.sdk.model.WireMessage
import com.wire.sdk.service.WireApplicationManager
import io.github.yamilmedina.app.api.CommandHandler
import io.github.yamilmedina.app.api.CommandMapper
import io.github.yamilmedina.app.api.HealthController
import io.github.yamilmedina.app.core.GetJiraIssue
import io.github.yamilmedina.app.core.JiraRepository
import io.github.yamilmedina.app.core.MessagingGateway
import io.github.yamilmedina.app.core.SendHelp
import io.github.yamilmedina.app.infra.config.JacksonConfig
import io.github.yamilmedina.app.infra.config.OutboundHttpClientConfig
import io.github.yamilmedina.app.infra.external.JiraHttpClient
import io.github.yamilmedina.app.infra.messaging.WireMessagingGateway
import io.github.yamilmedina.app.infra.repository.JiraRepositoryImpl
import org.koin.dsl.module
import java.util.*

// TODO: Split into separate module files per layer (api/ApiModule.kt, core/CoreModule.kt, infra/InfraModule.kt)
internal object ModulesConfig {

    /**
     * Infrastructure layer dependencies
     * Contains implementations of repositories, gateways, and external clients
     */
    private val infrastructure = module {
        // HTTP client configuration
        single { JacksonConfig.configure() }
        single { OutboundHttpClientConfig(get()) }

        // External clients
        single { JiraHttpClient(get()) }

        // Repository implementations (bind interface to implementation)
        single<JiraRepository> { JiraRepositoryImpl(get()) }

        // Gateway implementations (bind interface to implementation)
        single<MessagingGateway> { WireMessagingGateway(get()) }

        // Wire SDK setup
        single<WireApplicationManager> {
            val applicationId = UUID.randomUUID()
            val wireAppSdk = WireAppSdk(
                applicationId = applicationId,
                apiToken = "your-api-token", // TODO: Move to environment variable
                apiHost = "https://staging-nginz-https.zinfra.io", // TODO: Move to config
                cryptographyStoragePassword = "myDummyPasswordOfRandom32BytesCH", // TODO: Move to environment variable
                object : WireEventsHandlerDefault() {
                    override fun onMessage(wireMessage: WireMessage.Text) {
                        CommandMapper.mapCommands(wireMessage.conversationId, wireMessage.text)
                    }
                }
            )
            wireAppSdk.startListening()
            wireAppSdk.getApplicationManager()
        }
    }

    /**
     * Core/Domain layer dependencies
     * Contains use cases that depend only on interfaces
     */
    private val core = module {
        single { GetJiraIssue(get(), get()) }
        single { SendHelp(get()) }
    }

    /**
     * API/Presentation layer dependencies
     * Contains controllers and handlers
     */
    private val api = module {
        single { HealthController() }
        single { CommandHandler(get(), get()) }
    }

    val allModules = listOf(
        infrastructure,
        core,
        api
    )
}

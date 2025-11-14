package com.wire.apps.jira.di

import com.wire.apps.jira.api.CommandHandler
import com.wire.apps.jira.api.CommandMapper
import com.wire.apps.jira.api.HealthController
import com.wire.apps.jira.core.GetJiraIssue
import com.wire.apps.jira.core.JiraRepository
import com.wire.apps.jira.core.MessagingGateway
import com.wire.apps.jira.core.SendHelp
import com.wire.apps.jira.infra.config.JacksonConfig
import com.wire.apps.jira.infra.config.OutboundHttpClientConfig
import com.wire.apps.jira.infra.external.JiraHttpClient
import com.wire.apps.jira.infra.messaging.WireMessagingGateway
import com.wire.apps.jira.infra.repository.JiraRepositoryImpl
import com.wire.sdk.WireAppSdk
import com.wire.sdk.WireEventsHandlerDefault
import com.wire.sdk.model.WireMessage
import com.wire.sdk.service.WireApplicationManager
import org.koin.dsl.module
import java.util.UUID

// TODO: Split into separate module files per layer (api/ApiModule.kt, core/CoreModule.kt, infra/InfraModule.kt)
internal object ModulesConfig {
    /**
     * Infrastructure layer dependencies
     * Contains implementations of repositories, gateways, and external clients
     */
    private val infrastructure =
        module {
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
                val wireAppSdk =
                    WireAppSdk(
                        applicationId = applicationId,
                        // TODO: dummy use real with to environment variable
                        apiToken = "your-api-token",
                        // TODO: dummy use real with to environment variable
                        apiHost = "https://staging-nginz-https.zinfra.io",
                        // TODO: dummy use real with to environment variable
                        cryptographyStoragePassword = "myDummyPasswordOfRandom32BytesCH",
                        object : WireEventsHandlerDefault() {
                            override fun onTextMessageReceived(wireMessage: WireMessage.Text) {
                                CommandMapper.mapCommands(wireMessage.conversationId, wireMessage.text)
                            }
                        },
                    )
                wireAppSdk.startListening()
                wireAppSdk.getApplicationManager()
            }
        }

    /**
     * Core/Domain layer dependencies
     * Contains use cases that depend only on interfaces
     */
    private val core =
        module {
            single { GetJiraIssue(get(), get()) }
            single { SendHelp(get()) }
        }

    /**
     * API/Presentation layer dependencies
     * Contains controllers and handlers
     */
    private val api =
        module {
            single { HealthController() }
            single { CommandHandler(get(), get()) }
        }

    val allModules =
        listOf(
            infrastructure,
            core,
            api,
        )
}

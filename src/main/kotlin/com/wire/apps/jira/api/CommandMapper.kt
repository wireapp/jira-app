package com.wire.apps.jira.api

import com.wire.sdk.model.QualifiedId
import org.koin.core.component.KoinComponent
import org.slf4j.LoggerFactory

/**
 * Maps Wire SDK commands to the CommandHandler.
 * This acts as an adapter between Wire SDK types and domain types.
 */
object CommandMapper : KoinComponent {
    private val logger = LoggerFactory.getLogger(CommandMapper::class.java)

    private val commandHandler: com.wire.apps.jira.api.CommandHandler by lazy { getKoin().get() }

    fun mapCommands(
        conversationId: QualifiedId,
        rawCommand: String,
    ) {
        logger.debug("Mapping command from Wire SDK: $rawCommand")

        // Convert Wire SDK QualifiedId to String representation for domain layer
        val conversationIdString = "${conversationId.id}@${conversationId.domain}"

        commandHandler.handleCommand(conversationIdString, rawCommand)
    }
}

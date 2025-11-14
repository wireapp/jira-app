package com.wire.apps.jira.infra.messaging

import com.wire.apps.jira.core.MessagingGateway
import com.wire.apps.jira.core.exceptions.MessagingException
import com.wire.sdk.model.QualifiedId
import com.wire.sdk.model.WireMessage
import com.wire.sdk.service.WireApplicationManager
import org.slf4j.LoggerFactory

/**
 * Implementation of MessagingGateway using Wire SDK.
 * Handles message sending and error handling.
 */
class WireMessagingGateway(
    private val wireApplicationManager: WireApplicationManager,
) : MessagingGateway {
    private val logger = LoggerFactory.getLogger(WireMessagingGateway::class.java)

    override fun sendMessage(
        conversationId: String,
        message: String,
    ) {
        try {
            logger.debug("Sending message to conversation: $conversationId")
            val qualifiedId = parseQualifiedId(conversationId)
            wireApplicationManager.sendMessage(
                WireMessage.Text.create(qualifiedId, message),
            )
            logger.debug("Message sent successfully to conversation: $conversationId")
        } catch (e: Exception) {
            logger.error("Error sending message to conversation: $conversationId", e)
            throw MessagingException("Failed to send message to conversation: $conversationId", e)
        }
    }

    private fun parseQualifiedId(conversationId: String): QualifiedId {
        val parts = conversationId.split("@")
        if (parts.size != 2) {
            throw MessagingException("Invalid conversation ID format: $conversationId. Expected format: uuid@domain")
        }

        val uuid =
            try {
                java.util.UUID.fromString(parts[0])
            } catch (e: IllegalArgumentException) {
                throw MessagingException("Invalid UUID in conversation ID: ${parts[0]}", e)
            }

        return QualifiedId(uuid, parts[1])
    }
}

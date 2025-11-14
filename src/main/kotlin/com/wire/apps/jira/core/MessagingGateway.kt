package com.wire.apps.jira.core

/**
 * Gateway interface for sending messages to conversations.
 * This abstraction keeps the core layer independent of messaging infrastructure.
 */
interface MessagingGateway {
    /**
     * Sends a text message to a conversation.
     * @param conversationId The unique identifier for the conversation
     * @param message The text message to send
     */
    fun sendMessage(
        conversationId: String,
        message: String,
    )
}

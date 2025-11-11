package io.github.yamilmedina.app.api

import io.github.yamilmedina.app.core.GetJiraIssue
import io.github.yamilmedina.app.core.SendHelp
import io.github.yamilmedina.app.core.exceptions.InvalidCommandException
import org.slf4j.LoggerFactory

/**
 * Handler for processing commands in the API layer.
 * This follows proper layering by acting as an adapter between
 * the external world and use cases.
 */
class CommandHandler(
    private val getJiraIssue: GetJiraIssue,
    private val sendHelp: SendHelp
) {

    private val logger = LoggerFactory.getLogger(CommandHandler::class.java)

    /**
     * Processes a command from a conversation.
     * @param conversationId The conversation identifier
     * @param command The raw command string
     */
    fun handleCommand(conversationId: String, command: String) {
        logger.info("Handling command: $command for conversation: $conversationId")

        try {
            when {
                command.isBlank() -> {
                    throw InvalidCommandException("Command cannot be blank")
                }

                command.startsWith("/jira help") -> {
                    handleHelpCommand(conversationId)
                }

                command.startsWith("/jira ") -> {
                    handleJiraIssueCommand(conversationId, command)
                }

                else -> {
                    throw InvalidCommandException(command)
                }
            }
        } catch (e: Exception) {
            logger.error("Error handling command: $command", e)
            // Don't throw - this is an event handler, throwing would crash the app
            // In a real implementation, you might want to send an error message back
        }
    }

    private fun handleHelpCommand(conversationId: String) {
        logger.info("Help command requested for conversation: $conversationId")
        sendHelp(conversationId)
    }

    private fun handleJiraIssueCommand(conversationId: String, command: String) {
        val issueKey = command.removePrefix("/jira ").trim()

        if (!issueKey.matches(Regex("[A-Z]+-\\d+"))) {
            throw InvalidCommandException("Invalid Jira issue key format: $issueKey")
        }

        logger.info("Fetching Jira issue: $issueKey")
        getJiraIssue(conversationId to issueKey)
    }
}

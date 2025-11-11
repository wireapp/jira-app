package io.github.yamilmedina.app.core

import org.slf4j.LoggerFactory

class SendHelp(
    private val messagingGateway: MessagingGateway
) : UseCase<String, Unit> {

    private val logger = LoggerFactory.getLogger(SendHelp::class.java)

    override fun invoke(input: String) {
        val message = formatMessage()
        messagingGateway.sendMessage(conversationId = input, message = message)
        logger.info("Help message sent to conversation: $input")
    }

    private fun formatMessage(): String {
        return buildString {
            appendLine("### 🆘 Jira App quick guide")
            appendLine()
            appendLine("To use this app, you can send the following commands:")
            appendLine("- `/jira ISSUE-KEY`: Retrieves details for the specified Jira issue.")
            appendLine("- `/jira help`: Displays this help message.")
            appendLine()
        }
    }

}
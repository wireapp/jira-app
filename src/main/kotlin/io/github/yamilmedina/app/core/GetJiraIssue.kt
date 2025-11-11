package io.github.yamilmedina.app.core

import org.slf4j.LoggerFactory

/**
 * Use case for retrieving a Jira issue and sending it to a conversation.
 * This use case is now independent of infrastructure details.
 */
class GetJiraIssue(
    private val jiraRepository: JiraRepository,
    private val messagingGateway: MessagingGateway
) : UseCase<Pair<String, String>, Unit> {

    private val logger = LoggerFactory.getLogger(GetJiraIssue::class.java)

    override fun invoke(input: Pair<String, String>) {
        val (conversationId, issueKey) = input

        logger.info("Fetching Jira issue: $issueKey for conversation: $conversationId")

        // Fetch issue from repository (domain layer)
        val jiraIssue = jiraRepository.getIssue(issueKey)

        // Format message
        val message = formatJiraIssueMessage(jiraIssue)

        // Send message via gateway
        messagingGateway.sendMessage(conversationId, message)

        logger.info("Successfully sent Jira issue: $issueKey to conversation: $conversationId")
    }

    private fun formatJiraIssueMessage(issue: JiraIssue): String {
        return buildString {
            appendLine("### 📋 ${issue.key}: ${issue.summary}")
            appendLine("- ⚙️ **Status**: ${issue.status}")
            appendLine("- 👤 **Assignee**: ${issue.assignee}")
            issue.sprintName?.let {
                appendLine("- 🏃 **Sprint**: $it")
            }
            issue.epicLink?.let {
                appendLine("- 🗂 **Epic**: $it")
            }
            appendLine("- 🌐 [Link](https://wearezeta.atlassian.net/browse/${issue.key})")
        }
    }
}
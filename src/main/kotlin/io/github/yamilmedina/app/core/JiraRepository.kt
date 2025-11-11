package io.github.yamilmedina.app.core

/**
 * Repository interface for accessing Jira issue data.
 * This abstraction keeps the core layer independent of infrastructure details.
 */
interface JiraRepository {
    /**
     * Retrieves a Jira issue by its key.
     * @param key The Jira issue key (e.g., "WPB-123")
     * @return The Jira issue domain model
     * @throws io.github.yamilmedina.app.core.exceptions.JiraIssueNotFoundException if the issue doesn't exist
     * @throws io.github.yamilmedina.app.core.exceptions.JiraConnectionException if there's a connection error
     */
    fun getIssue(key: String): JiraIssue
}

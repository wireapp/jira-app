package io.github.yamilmedina.app.infra.repository

import io.github.yamilmedina.app.core.JiraIssue
import io.github.yamilmedina.app.core.JiraRepository
import io.github.yamilmedina.app.core.exceptions.JiraConnectionException
import io.github.yamilmedina.app.core.exceptions.JiraIssueNotFoundException
import io.github.yamilmedina.app.infra.external.JiraHttpClient
import org.slf4j.LoggerFactory

/**
 * Implementation of JiraRepository using HTTP client.
 * Handles DTO to domain model mapping and error handling.
 */
class JiraRepositoryImpl(private val jiraHttpClient: JiraHttpClient) : JiraRepository {

    private val logger = LoggerFactory.getLogger(JiraRepositoryImpl::class.java)

    override fun getIssue(key: String): JiraIssue {
        return try {
            logger.debug("Fetching Jira issue: $key")
            val response = jiraHttpClient.getJiraIssue(key).join()

            // Map DTO to domain model
            JiraIssue(
                key = response.key,
                summary = response.fields.summary,
                epicLink = response.fields.parent?.key,
                status = response.fields.status.name,
                assignee = response.fields.assignee?.displayName ?: "Unassigned",
                sprintName = response.fields.sprint?.firstOrNull()?.name
            ).also {
                logger.debug("Successfully fetched Jira issue: ${it.key}")
            }
        } catch (e: Exception) {
            logger.error("Error fetching Jira issue: $key", e)
            when {
                e.message?.contains("404") == true -> throw JiraIssueNotFoundException(key)
                else -> throw JiraConnectionException("Failed to fetch Jira issue: $key", e)
            }
        }
    }
}

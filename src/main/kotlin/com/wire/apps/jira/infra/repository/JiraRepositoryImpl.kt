package com.wire.apps.jira.infra.repository

import com.wire.apps.jira.core.JiraRepository
import com.wire.apps.jira.core.exceptions.JiraConnectionException
import com.wire.apps.jira.core.exceptions.JiraIssueNotFoundException
import com.wire.apps.jira.core.model.JiraIssue
import com.wire.apps.jira.infra.external.JiraHttpClient
import org.slf4j.LoggerFactory

/**
 * Implementation of JiraRepository using HTTP client.
 * Handles DTO to domain model mapping and error handling.
 */
class JiraRepositoryImpl(
    private val jiraHttpClient: JiraHttpClient,
) : JiraRepository {
    private val logger = LoggerFactory.getLogger(JiraRepositoryImpl::class.java)

    override fun getIssue(key: String): JiraIssue =
        try {
            logger.debug("Fetching Jira issue: $key")
            val response = jiraHttpClient.getJiraIssue(key).join()

            // Map DTO to domain model
            JiraIssue(
                key = response.key,
                summary = response.fields.summary,
                epicLink = response.fields.parent?.key,
                status = response.fields.status.name,
                assignee = response.fields.assignee?.displayName ?: "Unassigned",
                sprintName =
                    response.fields.sprint
                        ?.firstOrNull()
                        ?.name,
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

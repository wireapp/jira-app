package com.wire.apps.jira.infra.external

import com.wire.apps.jira.infra.config.OutboundHttpClientConfig
import com.wire.apps.jira.infra.external.dto.JiraIssueResponseDTO
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

/**
 * HTTP client for fetching Jira issues from the Jira API.
 * Returns DTOs which should be mapped to domain models by the repository layer.
 */
class JiraHttpClient(
    private val httpClient: OutboundHttpClientConfig,
) {
    private val logger = LoggerFactory.getLogger(JiraHttpClient::class.java)

    val username = System.getenv("JIRA_USERNAME") ?: error("JIRA_USERNAME env variable is not set")
    val password = System.getenv("JIRA_TOKEN") ?: error("JIRA_TOKEN env variable is not set")
    val url = System.getenv("JIRA_URL") ?: error("JIRA_URL env variable is not set")

    fun getJiraIssue(key: String): CompletableFuture<JiraIssueResponseDTO> {
        logger.debug("Fetching Jira issue from API: $key")

        return httpClient
            .get(
                "$url/issue/$key",
                basicAuth = username to password,
                responseType = JiraIssueResponseDTO::class.java,
            ).whenComplete { _, error ->
                if (error != null) {
                    logger.error("Error fetching Jira issue: $key", error)
                } else {
                    logger.debug("Successfully fetched Jira issue from API: $key")
                }
            }
    }
}

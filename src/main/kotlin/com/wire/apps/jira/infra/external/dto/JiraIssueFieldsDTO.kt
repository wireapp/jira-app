package com.wire.apps.jira.infra.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

// === Only the fields you care about ===
@JsonIgnoreProperties(ignoreUnknown = true)
data class JiraIssueFieldsDTO(
    val summary: String, // Title
    val status: JiraStatusDTO,
    val creator: JiraUserDTO, // Author
    val assignee: JiraUserDTO?, // Can be null
    val parent: JiraEpicLinkDTO?, // Epic
    @JsonProperty("customfield_10007")
    val sprint: List<JiraSprintDTO>? = null, // Sprint field
)

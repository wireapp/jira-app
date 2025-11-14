package com.wire.apps.jira.infra.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// === Root Response ===
@JsonIgnoreProperties(ignoreUnknown = true)
data class JiraIssueResponseDTO(
    val key: String,
    val fields: JiraIssueFieldsDTO,
)

package com.wire.apps.jira.infra.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class JiraSprintDTO(
    val name: String,
)

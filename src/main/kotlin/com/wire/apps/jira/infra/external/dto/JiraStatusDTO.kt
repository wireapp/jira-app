package com.wire.apps.jira.infra.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// === Tiny nested models ===
@JsonIgnoreProperties(ignoreUnknown = true)
data class JiraStatusDTO(
    val name: String,
)

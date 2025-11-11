package io.github.yamilmedina.app.infra.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// === Tiny nested models ===
@JsonIgnoreProperties(ignoreUnknown = true)
data class JiraStatusDTO(
    val name: String
)
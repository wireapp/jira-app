package io.github.yamilmedina.app.infra.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class JiraEpicLinkDTO(
    val key: String
)
package io.github.yamilmedina.app.infra.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class JiraUserDTO(
    @JsonProperty("displayName") val displayName: String
)
package io.github.yamilmedina.app.core

data class JiraIssue(
    val key: String,
    val summary: String,
    val assignee: String,
    val status: String,
    val epicLink: String?,
    val sprintName: String?
)
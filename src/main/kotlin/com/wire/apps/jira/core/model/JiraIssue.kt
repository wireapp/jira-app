package com.wire.apps.jira.core.model

data class JiraIssue(
    val key: String,
    val summary: String,
    val assignee: String,
    val status: String,
    val epicLink: String?,
    val sprintName: String?,
)

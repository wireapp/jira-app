package io.github.yamilmedina.app.core.exceptions

/**
 * Base exception for all domain-related errors
 */
sealed class DomainException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Thrown when a Jira issue is not found
 */
class JiraIssueNotFoundException(key: String) : DomainException("Jira issue not found: $key")

/**
 * Thrown when there's a connection error with Jira
 */
class JiraConnectionException(message: String, cause: Throwable? = null) : DomainException(message, cause)

/**
 * Thrown when a messaging operation fails
 */
class MessagingException(message: String, cause: Throwable? = null) : DomainException(message, cause)

/**
 * Thrown when a command is invalid
 */
class InvalidCommandException(command: String) : DomainException("Invalid command: $command")

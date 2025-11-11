package io.github.yamilmedina.app.infra.config

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.Base64
import java.util.concurrent.CompletableFuture

class OutboundHttpClientConfig(private val mapper: ObjectMapper) {
    private val client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(20))
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build()

    // GET with optional auth
    fun <T> get(
        url: String,
        token: String? = null,
        basicAuth: Pair<String, String>? = null,
        responseType: Class<T>
    ): CompletableFuture<T> {
        val request = HttpRequest.newBuilder(URI.create(url))
            .GET()
            .apply {
                token?.let { header("Authorization", "Bearer $it") }
                basicAuth?.let { (u, p) ->
                    val auth = Base64.getEncoder().encode("$u:$p".toByteArray()).toString(Charsets.UTF_8)
                    header("Authorization", "Basic $auth")
                }
                header("Accept", "application/json")
            }
            .build()

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply { response ->
                val body = response.body()
                mapper.readValue(body, responseType)
            }
    }
}


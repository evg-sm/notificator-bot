package com.notificator.bot.cucumber.helper

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient

@Component
class WebClientHelper(
    private val webTestClient: WebTestClient
) {

    private lateinit var response: WebTestClient.ResponseSpec

    fun send(method: HttpMethod, uri: String, body: String? = null) {
        response = webTestClient
            .method(method)
            .uri { it.path(uri).build() }
            .withBody(body)
            .exchange()
    }

    fun expectStatus(status: Int) {
        response.expectStatus().isEqualTo(status)
    }

    fun expectBodyXml(body: String) {
        response.expectBody().xml(body)
    }

    private fun WebTestClient.RequestBodySpec.withBody(body: String?) = apply {
        if (body != null) {
            contentType(MediaType.APPLICATION_JSON)
            accept(MediaType.APPLICATION_JSON)
            bodyValue(body)
        }
    }
}
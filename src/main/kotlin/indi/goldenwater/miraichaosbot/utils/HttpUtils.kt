package indi.goldenwater.miraichaosbot.utils

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

val client = HttpClient(OkHttp)

suspend fun httpGet(url: String): String {
    val httpResponse = client.get<HttpStatement>(url).execute()
    return httpResponse.readText()
}
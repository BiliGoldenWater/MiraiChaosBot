package indi.goldenwater.miraichaosbot.utils

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

val client = HttpClient(OkHttp) {
    followRedirects = false
    expectSuccess = false
    HttpResponseValidator {
        handleResponseException {
            if (it !is RedirectResponseException) throw it
        }
    }
}

suspend fun <T> httpGet(url: String, block: suspend (response: HttpResponse) -> T): T {
    return client.get<HttpStatement>(url).execute(block)
}

suspend fun httpGet(url: String): HttpResponse {
    return client.get<HttpStatement>(url).execute()
}

suspend fun httpGetRedirectTarget(url: String): String? {
    return httpGet(url) { it.headers["Location"] }
}

suspend fun httpGetText(url: String): String {
    return httpGet(url).readText()
}

suspend fun httpGetFile(url: String): FileResult {
    val httpResponse = httpGet(url)

    val imageInputStream = httpResponse.readBytes().inputStream()
    val imageExternalResource = imageInputStream.toExternalResource()

    withContext(Dispatchers.IO) {
        imageInputStream.close()
    }

    return FileResult(
        httpResponse.request.url.toString(),
        imageExternalResource
    )
}

data class FileResult(
    val url: String,
    val file: ExternalResource
)
package indi.goldenwater.miraichaosbot.utils

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

val client = HttpClient(OkHttp)

suspend fun httpGet(url: String): String {
    val httpResponse = client.get<HttpStatement>(url).execute()
    return httpResponse.readText()
}

suspend fun httpGetFile(url: String): FileResult {
    val httpResponse = client.get<HttpStatement>(url).execute()


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
    val img: ExternalResource
)
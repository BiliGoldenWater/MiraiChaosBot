package indi.goldenwater.miraichaosbot.utils

suspend fun getRandomImage(): FileResult {
    return httpGetFile(
        httpGetRedirectTarget("https://iw233.cn/api/Random.php") ?: throw Exception("No target location")
    )
}
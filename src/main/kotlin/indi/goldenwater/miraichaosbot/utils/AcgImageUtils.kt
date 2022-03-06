package indi.goldenwater.miraichaosbot.utils

suspend fun getRandomImage(): FileResult {
    return httpGetFile("https://iw233.cn/api/Random.php")
}
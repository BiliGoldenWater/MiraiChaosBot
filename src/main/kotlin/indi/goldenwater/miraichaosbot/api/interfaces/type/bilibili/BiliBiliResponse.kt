package indi.goldenwater.miraichaosbot.api.interfaces.type.bilibili

@kotlinx.serialization.Serializable
data class BiliBiliResponse<T>(
    val code: Int,
    val message: String = "",
    val ttl: Int,
    val data: T? = null,
)

package indi.goldenwater.miraichaosbot.api.interfaces.type.neteasecloudmusic

import kotlinx.serialization.json.JsonObject

@kotlinx.serialization.Serializable
data class DArtist(
    var id: Long = 0,
    var name: String?,
    var picUrl: String?,
    var alias: Array<String> = emptyArray(),
    var albumSize: Int = 0,
    var picId: Long = 0,
    var img1v1Url: String?,
    var img1v1: Int = 0,
    var trans: JsonObject?,
)
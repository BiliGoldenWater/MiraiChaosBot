package indi.goldenwater.miraichaosbot.api.interfaces.type.neteasecloudmusic

@kotlinx.serialization.Serializable
data class DSearchData(
    @Suppress("ArrayInDataClass") var songs: Array<DSong> = emptyArray(),
    var songCount: Int = 0,
)
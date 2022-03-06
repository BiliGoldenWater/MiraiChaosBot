package indi.goldenwater.miraichaosbot.api.interfaces.type

@kotlinx.serialization.Serializable
data class DSearchData(
    var songs: Array<DSong> = emptyArray(),
    var songCount: Int = 0,
)
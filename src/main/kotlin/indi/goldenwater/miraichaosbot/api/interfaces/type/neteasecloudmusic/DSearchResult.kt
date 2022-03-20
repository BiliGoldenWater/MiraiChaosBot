package indi.goldenwater.miraichaosbot.api.interfaces.type.neteasecloudmusic

@kotlinx.serialization.Serializable
data class DSearchResult(
    var result: DSearchData?,
    var code: Int = 0
)
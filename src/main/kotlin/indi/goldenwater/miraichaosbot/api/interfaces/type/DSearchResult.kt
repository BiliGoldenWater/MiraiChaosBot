package indi.goldenwater.miraichaosbot.api.interfaces.type

@kotlinx.serialization.Serializable
data class DSearchResult(
    var result: DSearchData?,
    var code: Int = 0
)
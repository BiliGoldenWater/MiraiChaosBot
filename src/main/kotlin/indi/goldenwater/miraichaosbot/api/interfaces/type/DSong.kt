package indi.goldenwater.miraichaosbot.api.interfaces.type

@kotlinx.serialization.Serializable
data class DSong(
    var id: Long = 0,
    var name: String?,
    var artists: Array<DArtist> = emptyArray(),
    var album: DAlbum?,
    var duration: Long = 0,
    var copyrightId: Long = 0,
    var status: Int = 0,
    var alias: Array<String> = emptyArray(),
    var rtype: Int = 0,
    var ftype: Int = 0,
    var mvid: Long = 0,
    var fee: Int = 0,
    var rUrl: String?,
    var mark: Long = 0,
)
package indi.goldenwater.miraichaosbot.api.interfaces.type.neteasecloudmusic

@kotlinx.serialization.Serializable
data class DAlbum(
    var id: Long = 0,
    var name: String?,
    var artist: DArtist?,
    var publishTime: Long = 0,
    var size: Int = 0,
    var copyrightId: Long = 0,
    var status: Int = 0,
    var picId: Long = 0,
    var mark: Long = 0
)
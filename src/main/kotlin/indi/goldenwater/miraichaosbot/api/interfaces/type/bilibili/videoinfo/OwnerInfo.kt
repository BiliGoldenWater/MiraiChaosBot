package indi.goldenwater.miraichaosbot.api.interfaces.type.bilibili.videoinfo

@kotlinx.serialization.Serializable
data class OwnerInfo(
    /**
     * UP主mid
     */
    val mid: Long,
    /**
     * 	UP主昵称
     */
    val name: String,
    /**
     * 	UP主头像
     */
    val face: String,
)

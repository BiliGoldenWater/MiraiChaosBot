package indi.goldenwater.miraichaosbot.api.interfaces.type.bilibili.videoinfo

@kotlinx.serialization.Serializable
data class BiliBiliVideoInfo(
    val bvid: String,
    val aid: Long,
    val videos: Int,
    /**
     * 分区id
     */
    val tid: Int,
    /**
     * 子分区名
     */
    val tname: String,
    /**
     * 1 原创 2 转载
     */
    val copyright: Int,
    /**
     * 封面图片url
     */
    val pic: String,
    val title: String,
    val pubdate: Long,
    val ctime: Long,
    val desc: String,
//    val desc_v2: Array,
    val state: Int,
    /**
     * 总时长 单位秒
     */
    val duration: Long,
    /**
     * 如果撞车 撞车视频avid
     */
    val forward: Long = -1,
    /**
     * 活动id
     */
    val mission_id: Long = -1,
    /**
     * 番剧/影视视频转跳链接
     */
    val redirect_url: String = "",
//    val rights: Obj
    val owner: OwnerInfo,
//    val stat: Obj
    /**
     * 视频同步发布的动态文字
     */
    val dynamic: String,
    /**
     * 1P cid
     */
    val cid: Long,
//    val dimension: Obj  1P 分辨率
//    val no_cache: Boolean
//    val pages: Array  分P列表
//    val subtitle: Obj  字幕信息
//    val staff:Array 合作成员列表
//    val user_garb:Obj 装扮信息
)

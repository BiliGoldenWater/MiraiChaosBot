package indi.goldenwater.miraichaosbot.api.interfaces.type.genshin

data class ArtifactAttribute(
    var hp: Double = 0.0,
    var atk: Double = 0.0,
    var def: Double = 0.0,
    /**
     * Element Mastery
     */
    var em: Double = 0.0,
    var hpP: Double = 0.0,
    var atkP: Double = 0.0,
    var defP: Double = 0.0,
    /**
     * Element Recharge
     */
    var er: Double = 0.0,
    /**
     * Crit Rate
     */
    var cr: Double = 0.0,
    /**
     * Crit Damage
     */
    var cd: Double = 0.0
) {
    fun attNameToLocateStr(string: String): String {
        return when (string) {
            "hp" -> "生命值"
            "atk" -> "攻击力"
            "def" -> "防御力"

            "em" -> "元素精通"
            "hpP" -> "生命值%"
            "atkP" -> "攻击力%"
            "defP" -> "防御力%"

            "er" -> "元素充能效率"
            "cr" -> "暴击率"
            "cd" -> "暴击伤害"
            else -> ""
        }
    }
}

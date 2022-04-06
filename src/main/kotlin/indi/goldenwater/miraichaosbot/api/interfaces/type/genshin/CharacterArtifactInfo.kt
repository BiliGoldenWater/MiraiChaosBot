package indi.goldenwater.miraichaosbot.api.interfaces.type.genshin

data class CharacterArtifactInfo(
    val characterName: String = "",
    val buildName: String = "",

    val data: ArtifactAttribute = ArtifactAttribute(),
) {
    fun parseRecommendText(text: String): CharacterArtifactInfo {
        val recommends = text
            .split("\n")
            .filter { it != "" }
            .map { it.replaceFirst(Regex("""[0-9]+\. """), "") }
            .map { it.trim() }

        var priority = 1.0
        recommends.forEach { it ->
            when (it) {
                "Flat HP" -> this.data.hp = priority
                "Flat ATK" -> this.data.atk = priority
                "Flat DEF" -> this.data.def = priority

                "Elemental Mastery" -> this.data.em = priority
                "HP%" -> this.data.hpP = priority
                "ATK%" -> this.data.atkP = priority
                "DEF%" -> this.data.defP = priority

                "Energy Recharge" -> this.data.er = priority
                "Crit Rate / DMG" -> {
                    this.data.cr = priority
                    this.data.cd = priority
                }
                "Crit Rate" -> {
                    this.data.cr = priority
                }
                "Crit DMG" -> {
                    this.data.cd = priority
                }

                "DEF% / ATK%" -> {
                    this.data.defP = priority
                    this.data.atkP = priority
                }
            }
            priority -= 0.1
        }
        return this
    }
}

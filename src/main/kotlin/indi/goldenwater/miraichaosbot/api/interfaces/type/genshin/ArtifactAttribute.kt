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
)

package indi.goldenwater.miraichaosbot.command

enum class Commands(private val regex: Regex) {
    Help(Regex("help", RegexOption.IGNORE_CASE)),

    Song(Regex("song", RegexOption.IGNORE_CASE)),
    ById(Regex("byId", RegexOption.IGNORE_CASE)),
    Search(Regex("search", RegexOption.IGNORE_CASE)),

    RandomAcg(Regex("randomAcg", RegexOption.IGNORE_CASE)),

    GetNonactiveMembers(Regex("getNonactiveMembers", RegexOption.IGNORE_CASE)),
    AddWhiteList(Regex("addWhiteList", RegexOption.IGNORE_CASE)),
    RemoveWhiteList(Regex("removeWhiteList", RegexOption.IGNORE_CASE)),
    NeverTalk(Regex("neverTalk", RegexOption.IGNORE_CASE)),
    LastTalkBefore(Regex("lastTalkBefore", RegexOption.IGNORE_CASE)),
    ;

    fun r(): Regex {
        return regex
    }
}
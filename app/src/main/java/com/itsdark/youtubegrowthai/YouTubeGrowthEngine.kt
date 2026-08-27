package com.itsdark.youtubegrowthai

class YouTubeGrowthEngine {

    fun generate(topic: String): GrowthResult {

        val t = topic.trim().replace(Regex("\\s+"), " ")

        require(t.isNotEmpty()) {
            "Topic cannot be empty"
        }

        val hashtags = makeHashtags(t)
        val keywords = makeKeywords(t)

        return GrowthResult(

            titles = """
1. $t 🔥 Epic Moment!
2. $t — The Ultimate Showdown ⚡
3. $t 🔥 You Won't Expect This!
4. $t Edit That Goes HARD 💥
5. $t — Wait For The Ending!
6. $t 🔥 Insane Moment!
7. $t — Who Wins? 👀
8. $t Edit 🔥 Must Watch!
9. $t — Most Powerful Moment!
10. $t 🔥 This Is CRAZY!
""".trimIndent(),

            description = """
🔥 $t

An epic YouTube Shorts edit featuring $t!

Watch till the end and tell us what you think. 👀

Like 👍
Comment 💬
Subscribe 🔔

$hashtags
""".trimIndent(),

            hook = """
Wait... did you see that? 👀🔥
Watch till the end!
""".trimIndent(),

            hashtags = hashtags,

            keywords = keywords,

            thumbnailText = """
EPIC MOMENT 🔥
WHO WINS? 👀
INSANE EDIT 💥
""".trimIndent(),

            cta = """
Like 👍 Comment 💬 and Subscribe 🔔
Tell us your favorite moment!
""".trimIndent(),

            growthTips = """
1. Start the Short with the strongest scene.
2. Keep the first 1–2 seconds interesting.
3. Use a short title with the main topic.
4. Add relevant hashtags only.
5. End with a reason for viewers to comment.
""".trimIndent(),

            alternativeTitles = """
$t But It Gets CRAZY 🔥
The Craziest $t Edit ⚡
You Won't Believe This $t Moment! 💥
""".trimIndent()
        )
    }

    private fun makeHashtags(topic: String): String {

        val words = topic
            .split(" ")
            .map {
                it.replace(
                    Regex("[^A-Za-z0-9]"),
                    ""
                )
            }
            .filter {
                it.length >= 2
            }

        val result = mutableListOf<String>()

        for (word in words.take(5)) {
            result.add("#${word}")
        }

        result.addAll(
            listOf(
                "#Shorts",
                "#YouTubeShorts",
                "#Viral",
                "#Edit",
                "#Anime"
            )
        )

        return result
            .distinct()
            .take(10)
            .joinToString(" ")
    }

    private fun makeKeywords(topic: String): String {

        return listOf(
            topic,
            "$topic edit",
            "$topic shorts",
            "$topic video",
            "$topic edit shorts",
            "viral shorts",
            "YouTube Shorts",
            "anime edit"
        )
            .distinct()
            .joinToString(", ")
    }
}

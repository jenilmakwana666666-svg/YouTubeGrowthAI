package com.itsdark.youtubegrowthai

class YouTubeGrowthEngine {

    fun generate(topic: String): GrowthResult {

        val original = topic.trim()

        require(original.isNotEmpty()) {
            "Enter a topic first"
        }

        val clean = cleanTopic(original)
        val lower = clean.lowercase()

        val subject = makeSubject(clean)

        val titles = makeTitles(
            subject,
            lower
        )

        val description = makeDescription(
            subject,
            lower
        )

        val hook = makeHook(
            subject,
            lower
        )

        val hashtags = makeHashtags(
            subject,
            lower
        )

        val keywords = makeKeywords(
            subject,
            lower
        )

        return GrowthResult(
            titles = titles,
            description = description,
            hook = hook,
            hashtags = hashtags,
            keywords = keywords,

            thumbnailText = """
1. AURA UNLEASHED 🔥
2. TOO POWERFUL ⚡
3. THAT AURA! 👀
""".trimIndent(),

            cta = """
Which moment was the best? 👀
Like 👍 Comment 💬 and Subscribe 🔔
""".trimIndent(),

            growthTips = """
1. Put the most powerful scene in the first 1–2 seconds.
2. Keep the title short and curiosity-driven.
3. Use only relevant hashtags.
4. End with a question to encourage comments.
5. Make the thumbnail text very short.
""".trimIndent(),

            alternativeTitles = makeAlternativeTitles(
                subject,
                lower
            )
        )
    }

    // -------------------------
    // CLEAN TOPIC
    // -------------------------

    private fun cleanTopic(input: String): String {

        var text = input.trim()

        text = text
            .replace(Regex("\\s+"), " ")
            .replace(" s ", "'s ")
            .replace(" vs ", " vs ")
            .replace("VS", "vs")
            .replace(" Vs ", " vs ")

        return text
    }

    private fun makeSubject(
        topic: String
    ): String {

        return topic
            .split(" ")
            .joinToString(" ") { word ->

                if (
                    word.length <= 2 ||
                    word.all { it.isUpperCase() }
                ) {
                    word
                } else {
                    word.replaceFirstChar {
                        it.uppercase()
                    }
                }
            }
    }

    // -------------------------
    // TITLES
    // -------------------------

    private fun makeTitles(
        subject: String,
        lower: String
    ): String {

        val list = mutableListOf<String>()

        if (lower.contains("aura")) {

            list.add(
                "$subject Is On Another Level 🔥"
            )

            list.add(
                "That $subject Aura Is INSANE! ⚡"
            )

            list.add(
                "When $subject Unleashes His Aura 👀🔥"
            )

            list.add(
                "The Aura Difference Is CRAZY 💀"
            )

            list.add(
                "$subject — Pure Aura Energy 🔥"
            )

            list.add(
                "Nobody Has Aura Like This ⚡"
            )

            list.add(
                "This $subject Moment Goes HARD 🔥"
            )

            list.add(
                "The Most INSANE $subject Aura 👀"
            )

            list.add(
                "$subject's Aura Says It All 💀🔥"
            )

            list.add(
                "You Can't Ignore This Aura ⚡"
            )

        } else if (
            lower.contains("vs") ||
            lower.contains("versus")
        ) {

            list.add(
                "$subject — Who Wins? 👀🔥"
            )

            list.add(
                "$subject's Ultimate Battle ⚡"
            )

            list.add(
                "The $subject Showdown 🔥"
            )

            list.add(
                "$subject — This Fight Is INSANE 💀"
            )

            list.add(
                "Nobody Expected This $subject Battle 👀"
            )

            list.add(
                "$subject: The Final Showdown ⚡"
            )

            list.add(
                "Who Would Win In $subject? 🔥"
            )

            list.add(
                "$subject Battle Goes CRAZY 💥"
            )

            list.add(
                "The Most Epic $subject Fight 🔥"
            )

            list.add(
                "$subject — Wait For The Ending! 👀"
            )

        } else if (
            lower.contains("edit")
        ) {

            list.add(
                "$subject Edit That Goes HARD 🔥"
            )

            list.add(
                "This $subject Edit Is INSANE ⚡"
            )

            list.add(
                "$subject Edit — Pure Perfection 👀"
            )

            list.add(
                "The $subject Edit You Need To See 🔥"
            )

            list.add(
                "This Edit Changed Everything 💀"
            )

            list.add(
                "$subject Edit Hits Different ⚡"
            )

            list.add(
                "One Of The Best $subject Edits 🔥"
            )

            list.add(
                "$subject Edit — No Words Needed 👀"
            )

            list.add(
                "This $subject Edit Is Too Good 💥"
            )

            list.add(
                "$subject Edit You Can't Skip 🔥"
            )

        } else {

            list.add(
                "$subject — You Need To See This 🔥"
            )

            list.add(
                "The Most INSANE $subject Moment 👀"
            )

            list.add(
                "$subject Hits Different ⚡"
            )

            list.add(
                "Nobody Expected This From $subject 🔥"
            )

            list.add(
                "$subject — This Was CRAZY 💀"
            )

            list.add(
                "This $subject Moment Is Unforgettable 👀"
            )

            list.add(
                "The $subject Moment Everyone Talks About 🔥"
            )

            list.add(
                "$subject — Wait Until The End ⚡"
            )

            list.add(
                "You Won't Believe This $subject Moment 💥"
            )

            list.add(
                "$subject Was Built Different 🔥"
            )
        }

        return list
            .distinct()
            .mapIndexed { index, title ->
                "${index + 1}. $title"
            }
            .joinToString("\n")
    }

    // -------------------------
    // DESCRIPTION
    // -------------------------

    private fun makeDescription(
        subject: String,
        lower: String
    ): String {

        return if (lower.contains("aura")) {

            """
🔥 $subject

Some moments don't need an explanation — the aura says everything. ⚡

Watch till the end and experience the full moment. 👀

If you enjoyed it:
👍 Like
💬 Comment your favorite moment
🔔 Subscribe for more edits

#Shorts #Anime #Edit #Viral
""".trimIndent()

        } else if (
            lower.contains("vs") ||
            lower.contains("versus")
        ) {

            """
🔥 $subject

Two sides. One epic battle. ⚡
Who do you think takes the win?

Watch till the end and decide for yourself. 👀

👍 Like
💬 Comment your winner
🔔 Subscribe for more edits

#Shorts #Anime #Battle #Edit
""".trimIndent()

        } else {

            """
🔥 $subject

A moment worth watching till the end. 👀

If you enjoyed this:
👍 Like
💬 Comment your thoughts
🔔 Subscribe for more content

#Shorts #Edit #Viral #Trending
""".trimIndent()
        }
    }

    // -------------------------
    // HOOK
    // -------------------------

    private fun makeHook(
        subject: String,
        lower: String
    ): String {

        return when {

            lower.contains("aura") ->
                "You think you've seen aura? Wait until this moment. 👀🔥"

            lower.contains("vs") ||
            lower.contains("versus") ->
                "Only one can win... but who? 👀⚡"

            lower.contains("edit") ->
                "This edit gets better every second. 🔥"

            else ->
                "Wait until you see what happens next... 👀🔥"
        }
    }

    // -------------------------
    // HASHTAGS
    // -------------------------

    private fun makeHashtags(
        subject: String,
        lower: String
    ): String {

        val result = mutableListOf<String>()

        val words = subject
            .split(" ")
            .map {
                it.replace(
                    Regex("[^A-Za-z0-9]"),
                    ""
                )
            }
            .filter {
                it.length >= 3
            }

        words.take(4).forEach {
            result.add("#$it")
        }

        if (
            lower.contains("goku") ||
            lower.contains("vegeta") ||
            lower.contains("zamasu") ||
            lower.contains("anime")
        ) {
            result.add("#Anime")
            result.add("#DragonBall")
            result.add("#DragonBallSuper")
        }

        result.add("#Shorts")
        result.add("#YouTubeShorts")
        result.add("#Edit")
        result.add("#Viral")

        return result
            .distinct()
            .take(10)
            .joinToString(" ")
    }

    // -------------------------
    // KEYWORDS
    // -------------------------

    private fun makeKeywords(
        subject: String,
        lower: String
    ): String {

        val list = mutableListOf<String>()

        list.add(subject)
        list.add("$subject edit")
        list.add("$subject shorts")
        list.add("$subject video")
        list.add("viral $subject")
        list.add("$subject edit shorts")
        list.add("YouTube Shorts")

        if (
            lower.contains("goku") ||
            lower.contains("vegeta") ||
            lower.contains("zamasu")
        ) {
            list.add("Dragon Ball")
            list.add("Dragon Ball Super")
            list.add("anime edit")
        }

        return list
            .distinct()
            .joinToString(", ")
    }

    // -------------------------
    // ALTERNATIVE TITLES
    // -------------------------

    private fun makeAlternativeTitles(
        subject: String,
        lower: String
    ): String {

        return if (lower.contains("aura")) {

            """
1. The Aura Is Absolutely INSANE 🔥
2. This Aura Moment Hits Different ⚡
3. When The Aura Takes Over 👀
""".trimIndent()

        } else if (
            lower.contains("vs") ||
            lower.contains("versus")
        ) {

            """
1. The Battle Everyone Wanted 🔥
2. Who Is Really Stronger? 👀
3. This Fight Goes CRAZY ⚡
""".trimIndent()

        } else {

            """
1. This Moment Is INSANE 🔥
2. You Need To See This 👀
3. This Goes HARD ⚡
""".trimIndent()
        }
    }
}

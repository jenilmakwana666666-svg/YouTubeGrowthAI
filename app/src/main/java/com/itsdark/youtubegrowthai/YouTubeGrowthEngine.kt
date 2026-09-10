package com.itsdark.youtubegrowthai

class YouTubeGrowthEngine {

    // tone and category are optional and default to the app's original
    // behavior. variationSeed defaults to 0, which reproduces the exact
    // original deterministic output. Passing a non-zero seed (used by the
    // Regenerate feature) shuffles titles/hook/CTA choices for fresh results.
    fun generate(
        topic: String,
        tone: String = "Default",
        category: String = "General",
        variationSeed: Int = 0
    ): GrowthResult {

        val original = topic.trim()

        require(original.isNotEmpty()) {
            "Enter a topic first"
        }

        val clean = cleanTopic(original)
        val lower = clean.lowercase()

        val subject = makeSubject(clean)

        val random =
            if (variationSeed != 0)
                kotlin.random.Random(variationSeed)
            else
                null

        var titles = makeTitles(subject, lower)
        var hook = makeHook(subject, lower)
        var cta = """
Which moment was your favorite? 👀
Comment below and subscribe for more Shorts! 🔔
""".trimIndent()

        if (random != null) {

            titles = shuffleTitles(titles, random)
            hook = pickHookVariant(subject, lower, random)
            cta = applyToneToCta(cta, tone)

        } else if (tone != "Default") {

            cta = applyToneToCta(cta, tone)
        }

        return GrowthResult(
            titles = titles,
            description = makeDescription(subject, lower),
            hook = hook,
            hashtags = makeHashtags(subject, lower, category),
            keywords = makeKeywords(subject, lower),

            thumbnailText = makeThumbnailText(subject, lower),

            cta = cta,

            growthTips = """
1. Start with the strongest moment immediately.
2. Keep the first few seconds highly engaging.
3. Use a clear and curiosity-driven title.
4. Keep hashtags relevant to the actual video.
5. Give viewers a reason to comment.
""".trimIndent(),

            alternativeTitles = makeAlternativeTitles(subject, lower)
        )
    }

    // =========================================================
    // REGENERATE / TONE HELPERS (new)
    // =========================================================

    private fun shuffleTitles(
        titlesText: String,
        random: kotlin.random.Random
    ): String {

        val lines = titlesText
            .split("\n")
            .map { it.substringAfter(". ") }
            .toMutableList()

        lines.shuffle(random)

        return lines
            .mapIndexed { index, title ->
                "${index + 1}. $title"
            }
            .joinToString("\n")
    }

    private fun pickHookVariant(
        subject: String,
        lower: String,
        random: kotlin.random.Random
    ): String {

        val pool: List<String> = when {

            lower.contains("aura") -> listOf(
                "You think you've seen aura? Wait until this moment. 👀🔥",
                "Wait for it... this aura is unreal 👀🔥",
                "This aura hits different every time ⚡"
            )

            lower.contains(" vs ") ||
            lower.contains("battle") ||
            lower.contains("fight") -> listOf(
                "Only one can win... but who? 👀⚡",
                "Which one takes the win? 🔥👀",
                "This fight is closer than you think ⚡"
            )

            lower.contains("edit") -> listOf(
                "This edit gets better every second. Don't skip! 🔥",
                "Every second of this edit slaps 🔥",
                "This edit was worth the wait 👀"
            )

            else -> listOf(
                "Wait until you see what happens next... 👀🔥",
                "This moment changes everything 🔥",
                "You need to see how this ends 👀"
            )
        }

        return pool[random.nextInt(pool.size)]
    }

    private fun applyToneToCta(
        baseCta: String,
        tone: String
    ): String {

        return when (tone) {

            "Funny" ->
                "$baseCta\n😂 Don't forget to laugh and subscribe!"

            "Serious" ->
                baseCta

            "Motivational" ->
                "$baseCta\n💪 Keep pushing — subscribe for more inspiration!"

            "Educational" ->
                "Learned something new? 📚\nSubscribe for more helpful Shorts! 🔔"

            else ->
                baseCta
        }
    }

    // =========================================================
    // TOPIC CLEANING
    // =========================================================

    private fun cleanTopic(input: String): String {

        var text = input.trim()

        text = text.replace(
            Regex("\\s+"),
            " "
        )

        text = text.replace(
            Regex("(?i)\\s+s\\s+"),
            "'s "
        )

        text = text.replace(
            Regex("(?i)\\s+vs\\s+"),
            " vs "
        )

        text = text.replace(
            Regex("(?i)\\s+versus\\s+"),
            " vs "
        )

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

    // =========================================================
    // TITLES
    // =========================================================

    private fun makeTitles(
        subject: String,
        lower: String
    ): String {

        val titles = mutableListOf<String>()

        when {

            lower.contains("aura") -> {

                titles.add(
                    "$subject Is On Another Level 🔥"
                )

                titles.add(
                    "That Aura Is Absolutely INSANE ⚡"
                )

                titles.add(
                    "When The Aura Takes Over 👀🔥"
                )

                titles.add(
                    "This Aura Moment Hits Different 💀"
                )

                titles.add(
                    "The Power Behind This Aura Is CRAZY ⚡"
                )

                titles.add(
                    "$subject — Pure Aura Energy 🔥"
                )

                titles.add(
                    "Nobody Expected This Aura 👀"
                )

                titles.add(
                    "This Moment Has TOO Much Aura 💀🔥"
                )

                titles.add(
                    "The Aura Says Everything ⚡"
                )

                titles.add(
                    "You Can't Ignore This Aura 👀🔥"
                )
            }

            lower.contains(" vs ") -> {

                titles.add(
                    "$subject — Who Wins? 👀🔥"
                )

                titles.add(
                    "$subject: The Ultimate Showdown ⚡"
                )

                titles.add(
                    "Nobody Expected This $subject Battle 🔥"
                )

                titles.add(
                    "$subject Battle Goes CRAZY 💀"
                )

                titles.add(
                    "Who Would Win In $subject? 👀"
                )

                titles.add(
                    "The Most Epic $subject Fight 🔥"
                )

                titles.add(
                    "$subject — This Fight Is INSANE ⚡"
                )

                titles.add(
                    "The Final $subject Showdown 💥"
                )

                titles.add(
                    "$subject — Wait For The Ending! 👀"
                )

                titles.add(
                    "This $subject Battle Hits Different 🔥"
                )
            }

            lower.contains("edit") -> {

                titles.add(
                    "$subject Edit That Goes HARD 🔥"
                )

                titles.add(
                    "This $subject Edit Is INSANE ⚡"
                )

                titles.add(
                    "$subject Edit Hits Different 👀"
                )

                titles.add(
                    "The $subject Edit You Need To See 🔥"
                )

                titles.add(
                    "This $subject Edit Is Too Good 💀"
                )

                titles.add(
                    "$subject Edit — Pure Perfection ⚡"
                )

                titles.add(
                    "One Of The Best $subject Edits 🔥"
                )

                titles.add(
                    "$subject Edit You Can't Skip 👀"
                )

                titles.add(
                    "This Edit Goes HARD 💥"
                )

                titles.add(
                    "$subject — The Edit Everyone Needs To See 🔥"
                )
            }

            else -> {

                titles.add(
                    "$subject — You Need To See This 🔥"
                )

                titles.add(
                    "The Most INSANE $subject Moment 👀"
                )

                titles.add(
                    "$subject Hits Different ⚡"
                )

                titles.add(
                    "Nobody Expected This From $subject 🔥"
                )

                titles.add(
                    "$subject — This Was CRAZY 💀"
                )

                titles.add(
                    "This $subject Moment Is Unforgettable 👀"
                )

                titles.add(
                    "$subject — Wait Until The End ⚡"
                )

                titles.add(
                    "You Won't Believe This $subject Moment 💥"
                )

                titles.add(
                    "$subject Was Built Different 🔥"
                )

                titles.add(
                    "This $subject Moment Goes HARD 🔥"
                )
            }
        }

        return titles
            .distinct()
            .mapIndexed { index, title ->
                "${index + 1}. $title"
            }
            .joinToString("\n")
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    private fun makeDescription(
        subject: String,
        lower: String
    ): String {

        return when {

            lower.contains("goku") ||
            lower.contains("vegeta") ||
            lower.contains("zamasu") ||
            lower.contains("dragon ball") ||
            lower.contains("anime") -> {

                """
🔥 $subject

An epic anime moment you won't want to miss! ⚡
Watch till the end for the best part. 👀

What do you think about this moment?
Drop your opinion in the comments! 👇

👍 Like if you enjoyed the edit
💬 Comment your favorite moment
🔔 Subscribe for more anime Shorts

#itsdark #itsdark444 #Shorts #Anime #DragonBall #DragonBallSuper #AnimeEdit
""".trimIndent()
            }

            lower.contains(" vs ") ||
            lower.contains("battle") ||
            lower.contains("fight") -> {

                """
🔥 $subject

The ultimate showdown is here! ⚡
Watch till the end and decide who had the better moment. 👀

Who wins? Tell us in the comments! 👇

👍 Like
💬 Comment your winner
🔔 Subscribe for more epic Shorts

#itsdark #itsdark444 #Shorts #Battle #Edit #YouTubeShorts
""".trimIndent()
            }

            lower.contains("edit") -> {

                """
🔥 $subject

A clean edit featuring the best moments. ⚡
Watch till the end and let us know what you think! 👀

Which part was your favorite? 👇

👍 Like
💬 Comment
🔔 Subscribe for more edits and Shorts

#itsdark #itsdark444 #Shorts #Edit #AMV #YouTubeShorts
""".trimIndent()
            }

            lower.contains("minecraft") ||
            lower.contains("gaming") ||
            lower.contains("game") ||
            lower.contains("free fire") ||
            lower.contains("bgmi") ||
            lower.contains("pubg") -> {

                """
🎮 $subject

Watch till the end — the best moment is coming! 🔥

Did you expect that to happen? 👀
Tell us what you think in the comments! 👇

👍 Like
💬 Comment
🔔 Subscribe for more gaming Shorts

#itsdark #itsdark444 #Shorts #Gaming #Gameplay #YouTubeShorts
""".trimIndent()
            }

            else -> {

                """
🔥 $subject

Watch till the end for the best moment! 👀

What do you think about this?
Let us know in the comments! 👇

👍 Like
💬 Comment
🔔 Subscribe for more Shorts

#itsdark #itsdark444 #Shorts #YouTubeShorts #Trending
""".trimIndent()
            }
        }
    }

    // =========================================================
    // HOOK
    // =========================================================

    private fun makeHook(
        subject: String,
        lower: String
    ): String {

        return when {

            lower.contains("aura") ->
                "You think you've seen aura? Wait until this moment. 👀🔥"

            lower.contains(" vs ") ||
            lower.contains("battle") ||
            lower.contains("fight") ->
                "Only one can win... but who? 👀⚡"

            lower.contains("edit") ->
                "This edit gets better every second. Don't skip! 🔥"

            else ->
                "Wait until you see what happens next... 👀🔥"
        }
    }

    // =========================================================
    // HASHTAGS
    // =========================================================

    private fun makeHashtags(
        subject: String,
        lower: String,
        category: String = "General"
    ): String {

        val hashtags = mutableListOf<String>()

        // YOUR FIXED BRAND HASHTAGS
        hashtags.add("#itsdark")
        hashtags.add("#itsdark444")

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

        words
            .take(3)
            .forEach {
                hashtags.add("#$it")
            }

        when {

            lower.contains("goku") ||
            lower.contains("vegeta") ||
            lower.contains("zamasu") ||
            lower.contains("dragon ball") -> {

                hashtags.add("#DragonBall")
                hashtags.add("#DragonBallSuper")
                hashtags.add("#Anime")
            }

            lower.contains("anime") -> {

                hashtags.add("#Anime")
                hashtags.add("#AnimeEdit")
            }

            lower.contains("gaming") ||
            lower.contains("minecraft") ||
            lower.contains("free fire") ||
            lower.contains("bgmi") ||
            lower.contains("pubg") -> {

                hashtags.add("#Gaming")
                hashtags.add("#Gameplay")
            }

            lower.contains("edit") -> {

                hashtags.add("#Edit")
                hashtags.add("#AnimeEdit")
            }
        }

        when (category) {

            "Gaming" -> {
                hashtags.add("#Gaming")
                hashtags.add("#Gameplay")
            }

            "Tech" -> {
                hashtags.add("#Tech")
                hashtags.add("#TechShorts")
            }

            "Vlog" -> {
                hashtags.add("#Vlog")
                hashtags.add("#DailyVlog")
            }

            "Comedy" -> {
                hashtags.add("#Comedy")
                hashtags.add("#Funny")
            }

            "Anime" -> {
                hashtags.add("#Anime")
                hashtags.add("#AnimeEdit")
            }
        }

        hashtags.add("#Shorts")
        hashtags.add("#YouTubeShorts")

        return hashtags
            .distinct()
            .take(12)
            .joinToString(" ")
    }

    // =========================================================
    // KEYWORDS
    // =========================================================

    private fun makeKeywords(
        subject: String,
        lower: String
    ): String {

        val keywords = mutableListOf<String>()

        keywords.add(subject)
        keywords.add("$subject edit")
        keywords.add("$subject shorts")
        keywords.add("$subject video")
        keywords.add("$subject edit shorts")
        keywords.add("YouTube Shorts")

        if (
            lower.contains("goku") ||
            lower.contains("vegeta") ||
            lower.contains("zamasu")
        ) {
            keywords.add("Dragon Ball")
            keywords.add("Dragon Ball Super")
            keywords.add("anime edit")
            keywords.add("anime shorts")
        }

        if (
            lower.contains("gaming") ||
            lower.contains("minecraft") ||
            lower.contains("free fire") ||
            lower.contains("bgmi") ||
            lower.contains("pubg")
        ) {
            keywords.add("gaming shorts")
            keywords.add("viral gaming")
            keywords.add("gameplay")
        }

        return keywords
            .distinct()
            .joinToString(", ")
    }

    // =========================================================
    // THUMBNAIL TEXT
    // =========================================================

    private fun makeThumbnailText(
        subject: String,
        lower: String
    ): String {

        return when {

            lower.contains("aura") -> """
AURA UNLEASHED 🔥
TOO POWERFUL ⚡
THAT AURA! 👀
""".trimIndent()

            lower.contains(" vs ") -> """
WHO WINS? 👀
ULTIMATE BATTLE 🔥
INSANE FIGHT ⚡
""".trimIndent()

            lower.contains("gaming") ||
            lower.contains("minecraft") ||
            lower.contains("free fire") ||
            lower.contains("bgmi") ||
            lower.contains("pubg") -> """
WHAT JUST HAPPENED? 👀
INSANE PLAY 🔥
WAIT FOR IT! ⚡
""".trimIndent()

            else -> """
INSANE MOMENT 🔥
WAIT FOR IT! 👀
THIS IS CRAZY ⚡
""".trimIndent()
        }
    }

    // =========================================================
    // ALTERNATIVE TITLES
    // =========================================================

    private fun makeAlternativeTitles(
        subject: String,
        lower: String
    ): String {

        return when {

            lower.contains("aura") -> """

1. The Aura Is Absolutely INSANE 🔥
2. This Aura Moment Hits Different ⚡
3. When The Aura Takes Over 👀

""".trimIndent()

            lower.contains(" vs ") -> """

1. The Battle Everyone Wanted 🔥
2. Who Is Really Stronger? 👀
3. This Fight Goes CRAZY ⚡

""".trimIndent()

            lower.contains("edit") -> """

1. This Edit Is INSANE 🔥
2. The Edit Hits Different ⚡
3. You Can't Skip This Edit 👀

""".trimIndent()

            else -> """

1. This Moment Is INSANE 🔥
2. You Need To See This 👀
3. This Goes HARD ⚡

""".trimIndent()
        }
    }
}

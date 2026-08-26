package com.itsdark.youtubegrowthai

import android.content.Context

class YouTubeGrowthEngine(
    context: Context
) {

    private val modelManager = LocalModelManager(context)

    fun generate(topic: String): GrowthResult {
        val cleanTopic = topic.trim()

        require(cleanTopic.isNotEmpty()) {
            "Topic cannot be empty."
        }

        /*
         * The native GGUF inference layer is intentionally separated
         * from this controller. This project does not pretend that
         * a GGUF file can be executed by Kotlin alone.
         *
         * Until the native llama.cpp Android library is bundled,
         * generation uses a clearly labelled local template engine.
         * It does not use the internet, APIs, analytics or tracking.
         */

        val prompt = PromptBuilder.build(
            cleanTopic,
            OutputLanguage.HINGLISH
        )

        return LocalTemplateGenerator.generate(
            topic = cleanTopic,
            prompt = prompt,
            modelManager = modelManager
        )
    }
}

private object LocalTemplateGenerator {

    fun generate(
        topic: String,
        prompt: String,
        modelManager: LocalModelManager
    ): GrowthResult {

        val titles = """
            1. $topic 🔥 The Moment Everyone Will Remember!
            2. $topic — This Fight Was INSANE!
            3. What If $topic Went Even Further?
            4. $topic Explained in 30 Seconds!
            5. The Most Epic Part of $topic!
            6. $topic Edit That Hits Different 🔥
            7. You Won't Believe This $topic Moment!
            8. $topic — Power Level Goes Crazy!
            9. The Ultimate $topic Showdown!
            10. $topic 🔥 Anime Shorts You Need to See!
        """.trimIndent()

        val description =
            "🔥 $topic\n\n" +
            "A fast-paced anime Shorts idea focused on $topic. " +
            "Use strong visuals, quick cuts and a clear opening hook. " +
            "This description is generated offline and does not use live analytics."

        val hook =
            "Wait for the moment when $topic changes everything! 🔥"

        val hashtags =
            "#anime #animeshorts #animeedit #shorts #dragonball " +
            "#goku #vegeta #broly #naruto #bleach"

        val keywords =
            "$topic, anime shorts, anime edit, anime fight, " +
            "anime video, YouTube Shorts, anime moments, anime fans"

        val thumbnailText =
            "EPIC FIGHT!\n" +
            "WHO WINS?\n" +
            "INSANE POWER!"

        val cta =
            "Which side are you choosing? Comment below 👇 " +
            "Follow/subscribe for more anime Shorts!"

        val growthTips =
            """
            • Start with the strongest visual in the first second.
            • Keep the opening sentence short and curiosity-driven.
            • Use readable on-screen text.
            • Remove unnecessary pauses from the edit.
            • Test different title styles across multiple uploads.
            • Do not rely on fake trending claims or invented analytics.
            """.trimIndent()

        val alternativeTitles =
            """
            Question style: Can $topic get any crazier?
            Curiosity style: Nobody expected this from $topic!
            Challenge style: Who wins in $topic?
            Story style: The moment $topic changed everything.
            Hype style: $topic just went INSANE!
            """.trimIndent()

        return GrowthResult(
            titles = titles,
            description = description,
            hook = hook,
            hashtags = hashtags,
            keywords = keywords,
            thumbnailText = thumbnailText,
            cta = cta,
            growthTips = growthTips,
            alternativeTitles = alternativeTitles
        )
    }
}

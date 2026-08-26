package com.itsdark.youtubegrowthai

object PromptBuilder {

    private const val SYSTEM_PROMPT =
        "You are YouTube Growth AI, a specialized assistant for YouTube Shorts content creation. " +
        "Generate useful, original and natural titles, descriptions, hooks, hashtags, keywords, " +
        "thumbnail text, CTAs and growth suggestions based on the user's topic. " +
        "Do not claim that something is trending unless current data is available. " +
        "Do not invent analytics."

    fun build(topic: String, language: OutputLanguage): String {
        val languageInstruction = when (language) {
            OutputLanguage.ENGLISH ->
                "Write the output in natural English."
            OutputLanguage.HINDI ->
                "Write the output in natural Hindi using Devanagari script."
            OutputLanguage.HINGLISH ->
                "Write the output in natural Hinglish using simple Roman Hindi mixed with English."
        }

        return """
            $SYSTEM_PROMPT

            $languageInstruction

            User topic:
            $topic

            Target niche:
            Anime, Dragon Ball, Goku, Vegeta, Broly, Frieza,
            Naruto, Bleach, anime edits and YouTube Shorts.

            Return the result using exactly these section markers:

            [TITLES]
            1.
            2.
            3.
            4.
            5.
            6.
            7.
            8.
            9.
            10.

            [DESCRIPTION]

            [HOOK]

            [HASHTAGS]

            [KEYWORDS]

            [THUMBNAIL_TEXT]

            [CTA]

            [GROWTH_TIPS]

            [ALTERNATIVE_TITLES]

            Keep the suggestions relevant to the topic.
            Avoid fake statistics, fake trends and unsupported claims.
        """.trimIndent()
    }
}

enum class OutputLanguage {
    ENGLISH,
    HINDI,
    HINGLISH
}

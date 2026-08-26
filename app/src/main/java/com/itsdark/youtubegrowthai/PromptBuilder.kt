package com.itsdark.youtubegrowthai

object PromptBuilder {

    private const val SYSTEM_PROMPT =
        "You are YouTube Growth AI, a specialized assistant for YouTube Shorts content creation. " +
        "Generate useful, original and natural titles, descriptions, hooks, hashtags, keywords, " +
        "thumbnail text, CTAs and growth suggestions based on the user's topic. " +
        "Do not claim that something is trending unless current data is available. " +
        "Do not invent analytics."

    fun build(
        topic: String,
        language: OutputLanguage
    ): String {

        val languageInstruction = when (language) {
            OutputLanguage.ENGLISH ->
                "Generate natural English."

            OutputLanguage.HINDI ->
                "Generate natural Hindi in Devanagari."

            OutputLanguage.HINGLISH ->
                "Generate natural Hinglish using Roman Hindi mixed with English."
        }

        return """
            $SYSTEM_PROMPT

            $languageInstruction

            Topic:
            $topic

            Niche:
            Anime, Dragon Ball, Goku, Vegeta, Broly, Frieza,
            Naruto, Bleach, anime edits and YouTube Shorts.

            Generate:

            [TITLES]
            Exactly 10 title ideas.

            [DESCRIPTION]
            One SEO-friendly Shorts description.

            [HOOK]
            One strong opening hook.

            [HASHTAGS]
            Relevant hashtags.

            [KEYWORDS]
            Search keywords.

            [THUMBNAIL_TEXT]
            Short thumbnail text ideas.

            [CTA]
            A natural call to action.

            [GROWTH_TIPS]
            Basic practical growth suggestions.

            [ALTERNATIVE_TITLES]
            Alternative title styles.

            Never invent analytics.
            Never claim something is trending without current data.
        """.trimIndent()
    }
}

enum class OutputLanguage {
    ENGLISH,
    HINDI,
    HINGLISH
}

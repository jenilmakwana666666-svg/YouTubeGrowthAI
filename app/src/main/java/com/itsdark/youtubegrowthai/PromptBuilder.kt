package com.itsdark.youtubegrowthai

object PromptBuilder {

    fun build(
        topic: String,
        language: OutputLanguage
    ): String {

        return """
You are YouTube Growth AI.

Topic: $topic

Write the answer in simple Hinglish.

IMPORTANT:
You MUST write every section below.
Do not skip any section.
Do not write anything before [TITLES].

[TITLES]
1. Title
2. Title
3. Title
4. Title
5. Title

[DESCRIPTION]
Write one YouTube Shorts description.

[HOOK]
Write one strong hook.

[HASHTAGS]
Write 10 relevant hashtags.

[KEYWORDS]
Write relevant search keywords.

[THUMBNAIL_TEXT]
Write 3 short thumbnail text ideas.

[CTA]
Write one call to action.

[GROWTH_TIPS]
Give 3 simple growth tips.

[ALTERNATIVE_TITLES]
Write 3 alternative titles.

Now generate the complete answer.
        """.trimIndent()
    }
}

enum class OutputLanguage {
    ENGLISH,
    HINDI,
    HINGLISH
}

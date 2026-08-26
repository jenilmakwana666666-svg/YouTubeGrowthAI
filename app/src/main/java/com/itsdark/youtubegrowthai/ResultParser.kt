package com.itsdark.youtubegrowthai

data class GrowthResult(
    val titles: String,
    val description: String,
    val hook: String,
    val hashtags: String,
    val keywords: String,
    val thumbnailText: String,
    val cta: String,
    val growthTips: String,
    val alternativeTitles: String
)

object ResultParser {

    fun parse(text: String): GrowthResult {
        return GrowthResult(
            titles = section(text, "TITLES"),
            description = section(text, "DESCRIPTION"),
            hook = section(text, "HOOK"),
            hashtags = section(text, "HASHTAGS"),
            keywords = section(text, "KEYWORDS"),
            thumbnailText = section(text, "THUMBNAIL_TEXT"),
            cta = section(text, "CTA"),
            growthTips = section(text, "GROWTH_TIPS"),
            alternativeTitles = section(text, "ALTERNATIVE_TITLES")
        )
    }

    private fun section(text: String, name: String): String {
        val startMarker = "[$name]"
        val start = text.indexOf(startMarker, ignoreCase = true)

        if (start == -1) {
            return ""
        }

        val contentStart = start + startMarker.length

        val nextStart = text.indexOf(
            "[",
            startIndex = contentStart
        )

        val contentEnd =
            if (nextStart == -1) text.length else nextStart

        return text
            .substring(contentStart, contentEnd)
            .trim()
    }
}

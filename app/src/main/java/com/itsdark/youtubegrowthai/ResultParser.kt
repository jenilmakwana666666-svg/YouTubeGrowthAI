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

        val clean = text.trim()

        return GrowthResult(
            titles = section(clean, "TITLES"),
            description = section(clean, "DESCRIPTION"),
            hook = section(clean, "HOOK"),
            hashtags = section(clean, "HASHTAGS"),
            keywords = section(clean, "KEYWORDS"),
            thumbnailText = section(clean, "THUMBNAIL_TEXT"),
            cta = section(clean, "CTA"),
            growthTips = section(clean, "GROWTH_TIPS"),
            alternativeTitles = section(clean, "ALTERNATIVE_TITLES")
        )
    }

    private fun section(
        text: String,
        name: String
    ): String {

        val patterns = listOf(
            "[$name]",
            name,
            "$name:",
            "**$name**",
            "## $name"
        )

        var start = -1
        var markerLength = 0

        for (pattern in patterns) {

            val index =
                text.indexOf(
                    pattern,
                    ignoreCase = true
                )

            if (index >= 0) {
                start = index
                markerLength = pattern.length
                break
            }
        }

        if (start == -1) {
            return ""
        }

        val contentStart =
            start + markerLength

        val nextMarkers = listOf(
            "[TITLES]",
            "[DESCRIPTION]",
            "[HOOK]",
            "[HASHTAGS]",
            "[KEYWORDS]",
            "[THUMBNAIL_TEXT]",
            "[CTA]",
            "[GROWTH_TIPS]",
            "[ALTERNATIVE_TITLES]"
        )

        var contentEnd = text.length

        for (marker in nextMarkers) {

            val index =
                text.indexOf(
                    marker,
                    startIndex = contentStart,
                    ignoreCase = true
                )

            if (index >= 0 && index < contentEnd) {
                contentEnd = index
            }
        }

        return text
            .substring(
                contentStart,
                contentEnd
            )
            .trim()
    }
}

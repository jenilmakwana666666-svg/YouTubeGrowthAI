package com.itsdark.youtubegrowthai

class TitleScorer {

    data class TitleScore(
        val score: Int,
        val notes: List<String>
    )

    private val powerWords =
        listOf(
            "insane", "crazy", "unbelievable", "secret", "shocking",
            "best", "worst", "amazing", "epic", "viral", "instant",
            "proven", "ultimate", "easy", "free", "new", "why", "how"
        )

    fun score(title: String): TitleScore {

        val trimmed = title.trim()
        val notes = mutableListOf<String>()
        var points = 0

        if (trimmed.isEmpty()) {
            return TitleScore(0, listOf("Title is empty"))
        }

        val length = trimmed.length

        when {
            length in 40..60 -> {
                points += 25
                notes.add("Good length ($length chars)")
            }
            length in 25..39 || length in 61..70 -> {
                points += 15
                notes.add("Okay length ($length chars)")
            }
            else -> {
                notes.add("Length ($length chars) is too short or too long — aim for 40-60")
            }
        }

        val lower = trimmed.lowercase()

        val powerWordHits =
            powerWords.count { lower.contains(it) }

        if (powerWordHits > 0) {
            points += (powerWordHits * 8).coerceAtMost(20)
            notes.add("Contains $powerWordHits power word(s)")
        } else {
            notes.add("No strong power words found")
        }

        if (Regex("[0-9]").containsMatchIn(trimmed)) {
            points += 10
            notes.add("Contains a number — numbers boost clicks")
        }

        if (trimmed.contains("?")) {
            points += 10
            notes.add("Question format builds curiosity")
        }

        val emojiCount =
            trimmed.count {
                it.code > 0x1F300
            }

        if (emojiCount in 1..2) {
            points += 10
            notes.add("Good use of emoji ($emojiCount)")
        } else if (emojiCount > 2) {
            points += 3
            notes.add("Too many emojis can look spammy")
        } else {
            notes.add("No emoji — consider adding 1-2 for visual pop")
        }

        val capsWordCount =
            trimmed.split(" ")
                .count {
                    it.length > 2 && it == it.uppercase() && it.any { c -> c.isLetter() }
                }

        if (capsWordCount in 1..2) {
            points += 10
            notes.add("Emphasis via capitalization used well")
        } else if (capsWordCount > 2) {
            notes.add("Too many ALL-CAPS words can look like clickbait spam")
        }

        val wordCount =
            trimmed.split(Regex("\\s+")).size

        if (wordCount in 5..10) {
            points += 15
            notes.add("Good word count ($wordCount words)")
        } else {
            notes.add("Word count ($wordCount) — 5-10 words usually reads best")
        }

        return TitleScore(
            score = points.coerceIn(0, 100),
            notes = notes
        )
    }
}

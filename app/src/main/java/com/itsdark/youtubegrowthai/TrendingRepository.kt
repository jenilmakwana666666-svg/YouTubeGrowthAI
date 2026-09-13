package com.itsdark.youtubegrowthai

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TrendingRepository {

    data class TrendingData(
        val titles: List<String>,
        val tags: List<String>,
        val descriptions: List<String>,
        val keywords: List<String>
    )

    private val stopWords =
        setOf(
            "this", "that", "with", "from", "your", "have", "will",
            "just", "what", "when", "were", "they", "them", "then",
            "than", "into", "about", "here", "there", "video", "shorts",
            "short", "youtube", "the", "and", "for", "you", "are",
            "all", "new", "how", "why", "who", "our", "out", "get",
            "https", "http", "www", "com"
        )

    // Throws an Exception on any failure — caller must NOT fall back to
    // offline generation, per app design (show error only).
    fun fetchTrending(
        apiKey: String,
        topic: String,
        regionCode: String = "IN"
    ): TrendingData {

        val encodedQuery =
            URLEncoder.encode(topic, "UTF-8")

        val publishedAfter =
            isoDateDaysAgo(7)

        val searchUrl =
            "https://www.googleapis.com/youtube/v3/search" +
                "?part=snippet" +
                "&q=$encodedQuery" +
                "&type=video" +
                "&order=viewCount" +
                "&maxResults=15" +
                "&regionCode=$regionCode" +
                "&publishedAfter=$publishedAfter" +
                "&key=$apiKey"

        val searchJson =
            JSONObject(httpGet(searchUrl))

        if (searchJson.has("error")) {

            val message =
                searchJson.getJSONObject("error")
                    .optString("message", "YouTube API error")

            throw Exception(message)
        }

        val items = searchJson.optJSONArray("items")

        val titles = mutableListOf<String>()
        val videoIds = mutableListOf<String>()

        if (items != null) {

            for (i in 0 until items.length()) {

                val item = items.getJSONObject(i)
                val snippet = item.getJSONObject("snippet")

                titles.add(snippet.getString("title"))

                val idObj = item.optJSONObject("id")
                val videoId = idObj?.optString("videoId")

                if (!videoId.isNullOrEmpty()) {
                    videoIds.add(videoId)
                }
            }
        }

        val tags = mutableListOf<String>()
        val descriptions = mutableListOf<String>()

        if (videoIds.isNotEmpty()) {

            val idsParam =
                videoIds.take(15).joinToString(",")

            val videosUrl =
                "https://www.googleapis.com/youtube/v3/videos" +
                    "?part=snippet" +
                    "&id=$idsParam" +
                    "&key=$apiKey"

            val videosJson =
                JSONObject(httpGet(videosUrl))

            if (!videosJson.has("error")) {

                val videoItems = videosJson.optJSONArray("items")

                if (videoItems != null) {

                    for (i in 0 until videoItems.length()) {

                        val snippet =
                            videoItems.getJSONObject(i)
                                .getJSONObject("snippet")

                        val tagArray = snippet.optJSONArray("tags")

                        if (tagArray != null) {

                            for (j in 0 until tagArray.length()) {
                                tags.add(tagArray.getString(j))
                            }
                        }

                        val description =
                            snippet.optString("description", "")

                        if (description.isNotBlank()) {

                            val firstLines =
                                description
                                    .lineSequence()
                                    .filter { it.isNotBlank() }
                                    .take(2)
                                    .joinToString(" ")

                            if (firstLines.isNotBlank()) {
                                descriptions.add(firstLines)
                            }
                        }
                    }
                }
            }
        }

        if (titles.isEmpty()) {
            throw Exception("No trending results found for this topic")
        }

        val keywords =
            extractKeywords(
                titles = titles,
                tags = tags,
                descriptions = descriptions
            )

        return TrendingData(
            titles = titles.distinct().take(10),
            tags = tags.distinct().take(20),
            descriptions = descriptions.distinct().take(10),
            keywords = keywords
        )
    }

    // Frequency-based keyword extraction from real trending titles, tags
    // and descriptions — used to build the "optimized" description/hook/
    // thumbnail text for the Live Trending feature.
    private fun extractKeywords(
        titles: List<String>,
        tags: List<String>,
        descriptions: List<String>
    ): List<String> {

        val wordCounts = mutableMapOf<String, Int>()

        val allText =
            (titles + descriptions).joinToString(" ")

        val words =
            allText
                .lowercase()
                .split(Regex("[^a-z0-9']+"))
                .filter {
                    it.length >= 4 && it !in stopWords
                }

        words.forEach {
            wordCounts[it] = (wordCounts[it] ?: 0) + 1
        }

        // Tags are already curated keywords — weight them higher.
        tags.forEach { tag ->

            val cleaned =
                tag.lowercase().trim()

            if (cleaned.length >= 3 && cleaned !in stopWords) {
                wordCounts[cleaned] = (wordCounts[cleaned] ?: 0) + 3
            }
        }

        return wordCounts.entries
            .sortedByDescending { it.value }
            .map { it.key }
            .distinct()
            .take(15)
    }

    private fun httpGet(urlString: String): String {

        val connection =
            URL(urlString).openConnection() as HttpURLConnection

        return try {

            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val code = connection.responseCode

            val stream =
                if (code in 200..299)
                    connection.inputStream
                else
                    connection.errorStream

            stream.bufferedReader().use {
                it.readText()
            }

        } finally {
            connection.disconnect()
        }
    }

    private fun isoDateDaysAgo(days: Int): String {

        val millis =
            System.currentTimeMillis() -
                (days.toLong() * 24 * 60 * 60 * 1000)

        val sdf =
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.US
            )

        sdf.timeZone = TimeZone.getTimeZone("UTC")

        return sdf.format(Date(millis))
    }
}
